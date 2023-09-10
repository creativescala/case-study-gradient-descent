# Case Study: Machine Learning by Gradient Descent

## Introduction

This case study looks at gradient descent, and the application of gradient descent to machine learning. We look at gradient descent from a programming, rather than mathematical, perspective. We'll start with a simple example that describes the problem we're trying to solve and how gradient descent can be used to solve it. We'll then look at three methods to compute gradients, the core of the problem:

- numerical differentiation;
- symbolic differentiation; and
- automatic differentiation.


## Gradient Descent for Function Fitting

At the time of writing (September 2022), [Stable Diffusion](https://stablediffusionweb.com/) is one of the newest, and best, text-to-image programs. Give it a try! Enter some text and see what image you can get it to produce. It's certainly impressive, though the results are sometimes a little bit odd.

At it's core, Stable Diffusion and similar programs such as Midjourney, are functions. Remember the core idea of a function is that you put something in and get something back. In this case you put in text and get back an image. 

```scala
def stableDiffusion(prompt: String): Image = ??? // Magic goes here
```

What makes these functions particularly interesting is that parts of the function are learned from data. The data consists of examples of text and images associated with them. The general shape of the function is fixed but many parts of it, called weights, are adjusted so that, given input, the output becomes closer to that in the data used for learning.

An example will help make this clearer. Consider the function below. We'll call this function the *model*.

$$f(x, a) = a \sin(x)$$

In Scala we'd write

```scala
def f(x: Double, a: Double): Double = a * Math.sin(x)
```

This is a method with two parameters:

1. `x`, which is the usual `x` value; and
2. `a`, which is the amplitude (height) of the sine wave.

(Note that I'm defining Scala methods but using the term function. In mathematics we usually only deal with functions, but in Scala most of the time it's more idiomatic to write methods.)

We can see the effect of changing `a` in the demonstration below.

@:doodle(draw-basic-plot, Sine.drawBasicPlot)

Now we're going to assume we have some data, and our task will be to find the value of `a` that gives the function that best fits the data. We're going to make this concrete in just a moment, but a bit more terminology before we move on:

- the parameter `x` is the *input parameter*; and
- the parameter `a` is the *weight* or *learned parameter*.

Back to the problem. Let's assume we have some *training data*, shown as the blue points below. We want to find a function that fits the data. This, informally, means that we want a function that is close to the data points. If we have a data point with an `x` and `y` value we want `f(x)` to be close to `y`. In our case, we're going to assume that the model will be a good fit for the data if we can just find the right value of the learned parameter. So our task reduces to finding the value of the learned parameter that gives the best fit to the data. You can try this yourself in the example below.

@:doodle(draw-error-plot, Sine.drawErrorPlot)

So our task is to find the value of the learned parameter that gives a good fit to the data. To do this we need to

1. formalize what we mean by a good fit; and
2. define an algorithm that finds a value for the learned parameter that gives a good fit.

We'll tackle each in turn. 

To quantify how good a choice we've made for `a`, we will use the sum of the square of the distance between the function output and the `y` value of each data point in our data set. We'll call this quantity the *loss*, and the *loss function* the function that calculates the loss given a choice of `a`. 

This will become clearer with some code. For a single data point we can calculate the loss as

```scala
def pointLoss(a: Double, point: Point)(model: (Double, Double) => Double): Double = {
  val error = model(point.x, a) - point.y
  error * error
}
```

In mathematical notation we write

$$pointLoss(a, point, model) = || model(point.x, a) - point.y ||^2$$

Now to calculate the full loss we sum over all the data.

```scala
def loss(a: Double, data: List[Data])(model: (Double, Double) => Double): Double = {
  data.map(pt => pointLoss(a, pt)(model)).sum
}
```

In mathematical notation

$$ loss(a, data, model) = \sum_{pt \in data}pointLoss(a, pt, model)$$

The demo below allows you to adjust `a` and see how the the loss changes for some randomly choosen data. You should note that you can increase and decrease the loss by changing `a`.

@:doodle(draw-loss-plot, Sine.drawLossPlot)

So we have:

- some *training data* that gives us example inputs and outputs;
- a model with a parameter (`a`) that we want to learn in response to the data; and
- a way of measuring the quality of our current choice of parameter via the loss.

Now the final piece of the puzzle is to come up with an algorithm to adjust the parameter to reduce the loss. This gets us to gradient descent. The *gradient* of a function is just a fancy word for the function's slope. The basic idea of gradient descent is:

1. Find the gradient of the loss with respect to the parameter `a`. This tells us how changes in `a` relate to changes in loss.
2. Move `a` in the direction that reduces the loss.


To recap, our goal is to make the loss as small as possible. In technical jargon we say we're minimizing the loss function. We're going to do this by calculating the gradient of the loss function with respect to `a`, and then move `a` a small amount in the direction that reduces the loss. We then repeat this process, until we can't reduce the loss any more or we get bored.

Notice at this point I'm not giving details. As you've probably guessed, you're going to implement this and our first approach will be numerical differentiation.
