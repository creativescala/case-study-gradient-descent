# Numerical Differentiation

Numerical differentiation is perhaps the most obvious approach to finding a function's gradient (that is, differentiating it) if you're a programmer.

The gradient is just a fancy name for the slope, and the slope is "rise over run".

$$ gradient = \frac{rise}{run} $$

We can estimate the gradient of `f` at a point `x` using the equation

$$ gradient \approx \frac{f(x + h) - f(x)}{h} $$

where `h` is a small number. This is the essential idea behind numerical differentiation.

It's time to write some code. Let's implement numerical differentiation. Now you might think that you should implement a method that takes all of

- the function we're differentiating;
- the step `h`; and
- the point `x` at which we're differentiating.

Instead, however, I want you to implement a method with following signature:

```scala
def numericalDifferentiation(h: Double)(f: Double => Double): Double => Double
```

So the method is taking a function, and returns a function. The returned function takes in a point `x` and returns an approximation of the gradient at that point.

This is how differentiation is treated in mathematics: the derivative of a function is itself a function. If you've studied calculus, this is what the \\(\frac{d}{dx}\\) operator is doing.


## Calculating the Gradient of the Loss Function

Now we have a way to calculate the gradient of a function, we can calculate the gradient of the loss function. However, we need to get the loss function into a form that we can use with our `numerical Differentiation` method. In other words, we need to turn `loss`, a function of 3 parameters, into a function of a single parameter. (That single parameter would be `a`, as that's what we  can vary to reduce the loss and hence what we want to calculate the derivate with respect to.)

The secret to doing this is called *currying*, which is not the delicious dish you probably think of when you hear the word, but instead the idea that a function of two parameters can become a function of a single parameter returning another function of a single parameter. For example, if we have the function

```scala
val sum: (Double, Double) => Double = (x, y) => x + y
```

we can curry it to obtain

```scala
val curriedSum: Double => Double => Double = x => y => x + y
```

This is especially useful when parameter vary at different rates. For example, the data we're using doesn't change at any time, whereas we've constantly the parameter `a` to try to find the best value. I don't want to write any more as this is the main challenge here: implement a curried form of `loss` so that we can use it with `numericalDifferentation`.


## Gradient Descent

Now we're ready to put it all together. Here's a sketch of how gradient descent works:

- Start by choosing an initial value for the parameter `a`.
- Calculate the gradient of the loss given `a`. 
- Move `a` a small amount (the so called *step size*) in the direction that decreases the gradient.
- Repeat this process until the loss is very small, the loss is not decreasing quickly, or the process has gone on long enough.

I'm a bit vague on the details above. You can choose reasonable value for the step size and other values I have omitted, and thinking about what is reasonable will force you to get a better understanding of what's going on.

Implement gradient descent, and show that you can find a good value for `a` by using it.
