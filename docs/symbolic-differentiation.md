# Symbolic Differentiation

Symbolic differentiation is the second differentiation technique we'll look at. This is the technique you'll have learned if you have studied calculus. 

In symbolic differentiation we work directly on the expression that represents the function of interest, creating a new expression that represents the derivative of the function. For example, if we have a function

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

The sum rule tells us we can separately compute the derivatives of \\( 4x^2 \\), \\( 8x \\) and \\( 16 \\) and then add together the results. Going from fromt to back:

- The power rule tells us the derivative of \\(x^2 \\) is \\( 2x \\). The product rule tells us the derivative of \\( 4x^2 \\) is \\( 0x^2 + 4(2x) \\), which is \\( 8x \\).
- Applying the same reasoning as above tells us the derivative of \\( 8x \\) is \\( 0x + 8 \\), which is \\( 8 \\).
- The constant rule tells us the derivative of \\( 16 \\) is \\( 0 \\)

Summing them all together gives us \\( 8x + 8 \\) as expected.

Note that we don't really need the power rule! \\( x^2 \\) is the same as \\( x * x \\), and we can correctly calculate the derivative using the product rule. It's a useful shortcut, but we're going to work without in our code below.

## Implementation 

Now we're ready to implement symbolic differentiation. We need:

1. a representation of expressions that we can manipulate; and
2. a way to implement the rules of differentiation.


### Representing Expressions

We're going to work with fairly simple expressions, like

$$ x^2 + 4 $$

and

$$ ax + 2x + 8 $$

These expressions consist of

- numbers, like `2` and `8`;
- variables, like `a` and `x`;
- additions and multiplication.

We're going to represent expressions as follows. An `Expression` is one of:

- A literal number, containing a `Double` value.
- A variable, containing a `String` name, which represents a variable such as \\( x \\) or \\( a \\) in our function. 
- The addition of two `Expressions`.
- The multiplication of two `Expressions`.

By now you should know how to represent this structure in code, so go ahead and implement it. There is a skeleton in `Expression.scala` to get you started.

In [numerical differentiation](numerical-differentiation.md) we represented mathematical functions as Scala functions. In this section we're representing mathematical functions as Scala data structures. This is an example of a general implementation strategy called *reification*. The abstract meaning of reification is to turn something abstract into something concrete. The concrete meaning, in our case, is to turn functions (or equivalently, methods) into data structures. Reification connects the functional programming world to the object oriented world. We'll see more of this in the future.


## Differentiating Expressions

With a representation of expressions we can go ahead and implement differentiation. Implement the method `differentiate` on `Expression`. This is perhaps the most complex task here, so you might choose to tackle the problems below before you get to differentiation.


## Binding Variables

Our expressions contains variables, such as `a` and `x` in \\( ax + 2x + 8 \\). When a variable hasn't taken on a particular value we call it a *free variable*. It's free in that it can take on any value.

We need to be able to give values to variables. For example, we might want to say that `x` is `1` in \\( ax + 2x + 8 \\). We'll want to do this when we come to evaluate the derivative at a particular point. Variables that have taken on a fixed value are called *bound variables*, and hence we say we bind a variable to a value.

Note that binding a variable to a value in an expression still leaves us with an expression, not just a number as you may expect. Considering binding `x` to `1` in \\( ax + 2x + 8 \\). We're left with \\( (a \times 1) + (2 \times 1) + 8 \\).

Implement a method `bind` that accepts both a variable name (a `String`) and a value for that variable (a `Double`), and substitutes the value for all occurrences of the variable within the `Expression`.


## Simplifying Expressions

After binding a variable to a value, we'll usually end up with a lot of constant expressions: multiplications or additions that involve only literals. We saw that in the example above where binding `x` to `1` in \\( ax + 2x + 8 \\) left us with \\( (a \times 1) + (2 \times 1) + 8 \\), when idealy we would want just \\( a + 2 + 8 \\) or even just \\( a + 10 \\).

To do this we want to implement a method to *simplify* expressions. This, like binding a variable, is an `Expression` to `Expression` transform. Simplification is as complex a problem as we care to make it. In this instance we're going to keep it very simple: we just want to simplify additions and multiplications of literals. So if we have \\( x + (1 + 2) \\) we should end up \\( x + 3 \\), but if we have \\( (x + 1) + 2 \\) we won't make any change.

Using our very simple approach to simplification means it may be possible to simplify an expression a multiple times. For example, if we have the expression \\( (1 + 2) + 3 \\), the first simplification should give us \\( 3 + 3 \\), which we can then simplify again to reach \\( 6 \\). Once we have reached \\( 6 \\) no further simplifications are possible.

Our simple simplification algorithm misses many opportunities for simplification. For example, it won't simplify \\( (3 + x) + 5 \\) to \\( x + 8 \\). However it does have one very nice property. There only two possible results: we make a shorter expression or we make no change. This means our simplification is guaranteed to eventually reach a point where no further simplification can occur. In formal terms we can say simplification is *terminating*, and that the simplification method has a *fixed point*. (A fixed point of a function \\( f \\) is a value \\( x \\) such that \\(f(x) = x \\)).

Now go ahead an implement `simplify`. You may choose to implement the basic simplification we described above, or to iterate the basic algorithm until you reach a fixed point.


## Finishing Up

With all the above in place we have everything we need to differentiate an expression and calculate the gradient at a particular point. This in turn means we can implement gradient descent.


## Extension
