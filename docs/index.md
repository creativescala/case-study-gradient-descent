# Case Study: Machine Learning by Gradient Descent

## Introduction

This case study looks at gradient descent, and the application of gradient descent to machine learning. We look at gradient descent from a programming, rather than mathematical, perspective. We'll start with a simple example that describes the problem we're trying to solve and how gradient descent can be used to solve it. We'll then look at three methods to compute gradients, the core of the problem:

- numerical differentiation;
- symbolic differentiation; and
- automatic differentiation.


## Gradient Descent for Function Fitting

At the time of writing (September 2022), [Stable Diffusion](https://stablediffusionweb.com/) is one of the newest, and best, text-to-image programs. Give it a try! Enter some text and see what image yous can get it to produce. It's certainly impressive, though the results are still sometimes a little bit odd.

At it's core, Stable Diffusion and similar programs such as Midjourney, are functions. Remember the core idea of a function is that you put something and get something back. In this case you put in text and get back an image. What makes these functions particularly interesting is that parts of the function are learned from data. The data consists of example of text and images associated with them. The general shape of the function is fixed but many parts of it, called weights, are adjusted so that, given input, the output becomes closer to that in data used for learning.

An example will help make this clearer. Consider the function below. We'll call this function the *model*.

$$f(x, a) = a \sin(x)$$

In Scala we'd write

```scala
val model: (Double, Double) => Double = (x, a) => a * Math.sin(x)
```

The model is a function with two parameters:

1. `x`, which is the usual `x` value; and
2. `a`, which is the amplitude (height) of the sine wave.

(Note that I'm defining functions so that the code is closer to the mathematics, but we could equally use a Scala method.)

You can play with the demo below, to see how changing the value of `a` changes the model.

@:doodle(draw-basic-plot, Sine.drawBasicPlot)

Now imagine we have some data, which are pairs of `x` and `y` values. For each `x` value we have the `y` value we'd like the model to produce. We can adjust the value of `a` to bring the model closer or further away from the output. To quantify how good a choice we've made for `a`, we can look at the distance between the model output and the `y` value for each data point in our data set. We'll call this the *loss function* or just the *loss*. The demo below allows you to adjust `a` and see how the the loss changes for some randomly choosen data. You should note that you can increase and decrease the loss by changing `a`.

@:doodle(draw-error-plot, Sine.drawErrorPlot)

So we have:

- some *training data* that gives us example inputs and outputs;
- a function with a parameter (`a`) that we want to learn in response to data; and
- a way of measuring the quality of our current choice of parameter (the loss);

Now the final piece of the puzzle is to tell the computer how to adjust the parameter to reduce the loss. This gets us to gradient descent. The *gradient* of a function is just a fancy word for the function's slope. So what we need to do is calculate the gradient of the loss function with respect to `a`, meaning:

1. find how changes in `a` relate to changes in loss, and
2. move `a` in the direction that reduces the loss.

We use the term *differentiation* for finding the gradient of a function.

To really formalize this we need to be a bit more precise about about the error function. For one particular data point, the loss is

$$ pointLoss(f, a, x, y) = (f(x, a) - y)^2 $$

In Scala, we'd write

```scala
val pointLoss: ((Double, Double) => Double, Double, Double, Double) => Double = 
  (f, a, x, y) => {
    val error = f(x, a) - y
    error * error
  }
```

This means the loss for a single point is always non-negative.


Now we just have to sum up the loss over all the data points to get what we commonly call the loss.

$$ loss(data, f, a) = \sum_{pt \in data}pointLoss(f, a, pt.x, pt.y)$$

You might not be familiar with this mathematical notation. It's not important. The Scala code is:

```scala
val loss: (List[Point], (Double, Double) => Double, Double) => Double =
  (data, f, a) => 
    data.foldLeft(0.0){ (accum, pt) => pointLoss(f, a, pt.x, pt.y) }
```

Because we're taking the sum of the point loss, the loss is also always non-negative. This means finding the smallest loss is always the best choice.

To recap, our goal is to make the loss as small as possible. In technical jargon we'd say we're minimizing the loss function. We're going to do this by calculating the gradient of the loss function with respect to `a`, and then move `a` a small amount in the direction that reduces the loss. We then repeat this process, until we can't reduce the loss any more or we get bored.

Notice at this point I'm not giving details. As you've probably guessed, you're going to implement this and our first approach will be numerical differentiation.
