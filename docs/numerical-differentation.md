# Numerical Differentiation

Numerical differentiation is perhaps the most obvious approach to finding a function's gradient (that is, differentiating it) if you're a programmer.

The gradient is just a fancy name for the slope, and the slope is "rise over run".

$$ gradient = \frac{rise}{run} $$

We can estimate the gradient of `f` at a point `x` using the equation

$$ gradient \approx \frac{f(x + h) - f(x)}{h} $$

where `h` is a small number. This is the essential idea behind numerical differentiation.


