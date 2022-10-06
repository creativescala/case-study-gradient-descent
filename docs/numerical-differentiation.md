# Numerical Differentiation

Numerical differentiation is perhaps the most obvious approach to finding a function's gradient (that is, differentiating it) if you're a programmer.

The gradient is just a fancy name for the slope, and the slope is "rise over run".

$$ gradient = \frac{rise}{run} $$

Using this we can estimate the gradient of `f` at a point `x` using the equation

$$ gradient \approx \frac{f(x + h) - f(x)}{h} $$

where \\( h \\) is a small number. In this equation \\( h \\) is the run, and \\( f(x+h) - f(x) \\) is the rise. This is the essential idea behind numerical differentiation.

The example below shows how the estimate of the gradient changes as we change \\( h \\) to be closer or further away from a given point (the point in black).

@:doodle(draw-numerical-differentiation-plot, Sine.drawNumericalDifferentiationPlot)


## Implementation

It's time to write some code. There is a [repository](https://github.com/creativescala/case-study-gradient-descent) that accompanies this case study. You should download it. Your code goes into the `code` subdirectory. Within that subdirectory you'll find some existing code to calculate loss, create data sets, and so other useful utilities. Make sure you take a look at it before you start creating your own code.

Let's implement numerical differentiation. Now you might think that you should implement a method that takes all of

- the function we're differentiating;
- the step `h`; and
- the point `x` at which we're differentiating.

Instead, however, I want you to implement a method with following signature:

```scala
def differentiate(h: Double)(f: Double => Double): Double => Double
```

So the method is taking a function, and returns a function. The returned function takes in a point `x` and returns an approximation of the gradient at that point.

This is how differentiation is treated in mathematics: the derivative of a function is itself a function. If you've studied calculus, this is what the \\(\frac{d}{dx}\\) operator is doing.

There is a code stub for you to work with in the file `NumericalDifferentiation.scala` within the `numerical` subdirectory. All your code should go within the `numerical` subdirectory. There are also a few tests you can use to check your implementation. You may want to add more tests.


### Calculating the Gradient of the Loss Function

Now we have a way to calculate the gradient of a function, we can calculate the gradient of the loss function. However, we need to get the loss function into a form that we can use with our `numericalDifferentiation` method. In other words, we need to turn `loss`, a function of 3 parameters, into a function of a single parameter. (That single parameter would be `a`, as that's what we can vary to reduce the loss and hence what we want to calculate the derivate with respect to.)

The secret to doing this is called *currying*, which is not the delicious dish you probably think of when you hear the word, but instead the idea that a function of two parameters can become a function of a single parameter returning another function of a single parameter. For example, if we have the function

```scala
val sum: (Double, Double) => Double = (x, y) => x + y
```

we can curry it to obtain

```scala
val curriedSum: Double => Double => Double = x => y => x + y
```

This is especially useful when parameter vary at different rates. For example, the data we're using doesn't change at any time, whereas we've constantly the parameter `a` to try to find the best value. I don't want to write any more as this is the main challenge here: implement a curried form of `loss` so that we can use it with `numericalDifferentation`.

There is a stub in `Loss.scala` that you can work with.


### Gradient Descent

Now we're ready to put it all together and implement gradient descent. We're going to start by implementing a method that performs a single iteration of gradient descent. This method with take in the current value of the parameter we're optimizing, and the function we're optimizing, and return an updated value for the parameter. See the `iterate` method on `GradientDescent.scala`.

In the body of the method we want to:

1. Calculate the gradient of the function at the value of the parameter, using the numerical differentiation method we have already implemented.
2. Calculate the updated value of the parameter.

How do we calculate the updated value? We want to adjust the parameter to reduce the loss, which means moving it a little bit in the direction of *negative* gradient. So if \\( x \\) is the current value of the parameter we want

$$ updatedX = x - (r \times gradient) $$

where \\( r \\) is a small value (such as 0.01) known as the *learning rate*.

Go ahead and implement this!

Now we can implement the full gradient descent algorithm, in the method `gradientDescent`. The only thing you need to do here is iterate for the given number of iterations. (There are other stopping conditions we can use, such as stopping when the loss doesn't decrease from one iteration to the next, but we're keeping things simple here.)


### Animate It!

Finally, complete the implementation in `Animation.scala` and draw an animation showing gradient descent at work!
