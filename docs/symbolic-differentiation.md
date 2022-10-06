# Symbolic Differentiation

The second technique that we'll look at for differentiation is symbolic differentiation. This is the technique you'll have learned if you have studied calculus. 

In symbolic differentiation we work directly on the expression that represents the function of interest, and create a new expression that represents the derivative of the function. For example, if we have a function

$$ f(x) = 4x^2 + 8x + 16 $$

we can symbolically differentiate it to obtain the derivative, itself a function and written as \\( \frac{df(x)}{dx} \\)

$$ \frac{df(x)}{dx} = 8x + 8 $$

Note that the derivative of a function is a function, in this case with a single parameter \\( x \\).

To compute the derivative we can apply the following rules:

- **The power rule:** the derivative of \\( x \\) to the power of \\( n \\) is \\(x \\) to the power of \\( n - 1 \\) multiplied by \\( n \\). 

$$ \textrm{If} \; f(x) = x^n \; \textrm{then} \; \frac{df(x)}{dx} = nx^{n-1} $$

- **The constant rule:** the derivative of a constant is zero. 

$$ \textrm{If}\;  f(x) = k \; \textrm{then}\; \frac{df(x)}{dx} = 0 $$

- **The sum rule:** the derivative of the sum of two functions is the sum of the derivatives.

$$ \textrm{If}\; f(x) = g(x) + h(x) \; \textrm{then}\; \frac{df(x)}{dx} = \frac{dg(x)}{dx} + \frac{dh(x)}{dx} $$

- **The product rule:** The derivative of the product of two functions is the derivative of the first multiplied by the second plus the derivative of the second multiplied by the first.

$$ \textrm{If}\; f(x) = g(x)h(x) \; \textrm{then}\; \frac{df(x)}{dx} = \frac{dg(x)}{dx}h(x) + g(x)\frac{dh(x)}{dx} $$


We can apply these rules to calculate the derivative of

$$ f(x) = 4x^2 + 8x + 16 $$

The sum rule tells us we can separately compute the derivatives of \\( 4x^2 \\), \\( 8x \\) and \\( 16 \\) and then add together the results. Going from back to front:

- The constant rule tells us the derivative of \\( 16 \\) is \\( 0 \\)
- The power rule tells us the derivative of \\( x \\) is \\( 1 \\). The product rule tells us the derivative of \\( 8x \\) is \\( 0x + 8 \\), which is \\( 8 \\).
- Applying the same reasoning as above tells us the derivative of \\( 4x^2 \\) is \\( 8x \\)

Summing them all together gives us \\( 8x + 8 \\) as expected.

Note that we don't really need the power rule! \\( x^2 \\) is the same as \\( x * x \\), and we can correctly calculate the derivative using the product rule. It's a useful shortcut, but we're going to work without in our code below.

## Implementation 

Now we're ready to implement symbolic differentiation. We need:

1. a representation of expressions that we can manipulate; and
2. a way to implement the rules of differentiation.


### Representing Expressions

We're going to represent expressions as follows. An `Expression` one of:

- A literal number, containing a `Double` value.
- A parameter, containing a `String` name, which represents a variable such as \\( x \\) or \\( a \\) in our function. 
- The addition of two `Expressions`.
- The multiplication of two `Expressions`.

By now you should know how to represent this structure in code, so go ahead and implement it. There is a skeleton in `Expression.scala` to get you started.

In [numerical differentiation](numerical-differentiation.md) we represented mathematical functions as Scala functions. In this section we're representing mathematical functions as Scala data structures. This is an example of a general implementation strategy called *reification*. The abstract meaning of reification is to turn something abstract into something concrete. The concrete meaning, in our case, is to turn functions (or equivalently, methods) into data structures. Reification connects the functional programming world to the object oriented world. We'll see more of this in the future.


## Applying Arguments

We need to be able to apply arguments to our functions. Implement a method `apply` that accepts both a parameter name (a `String`) and a value for that parameter (a `Double`), and substitutes the value for all occurrences of the parameter within the method.

This is a good first step, but after applying we can end up with an `Expression` that has many constant expressions: additions or multiplications where both parts of the expression are literals. Implement a method `simplify` that gets rid of as many of these as you can.


## Differentiating Expressions

Now we're ready to implement differentiation. 
