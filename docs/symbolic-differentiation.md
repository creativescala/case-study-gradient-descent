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

- **The product fule:** The derivative of the product of two functions is the derivative of the first multiplied by the second plus the derivative of the second multiplied by the first.

$$ \textrm{If}\; f(x) = g(x)h(x) \; \textrm{then}\; \frac{df(x)}{dx} = \frac{dg(x)}{dx}h(x) + g(x)\frac{dh(x)}{dx} $$


We can apply these rules to calculate the derivative of

$$ f(x) = 4x^2 + 8x + 16 $$

The sum rule tells us we can separately compute the derivatives of \\( 4x^2 \\), \\( 8x \\) and \\( 16 \\) and then add together the results. Going from back to front:

- The constant rule tells us the derivative of \\( 16 \\) is \\( 0 \\)
- The power rule tells us the derivative of \\( x \\) is \\( 1 \\). The product rule tells us the derivative of \\( 8x \\) is \\( 0x + 8 \\), which is \\( 8 \\).
- Applying the same reasoning as above tells us the derivative of \\( 4x^2 \\) is \\( 8x \\)

Summing them all together gives us \\( 8x + 8 \\) as expected.


## Implementation 

Now we're ready to implement symbolic differentiation. We need:

1. a representation of expressions that we can manipulate; and
2. a way to implement the rules of differentiation.


### Representing Expressions

We're going to represent expressions as follows. An `Expression` one of:

- A literal number, containing a `Double` value.
- A variable, which represents the parameter \\( x \\) in an expression. We're only considering functions of a single variable, so we only need one of these.
- The addition of two `Expressions`.
- The multiplication of two `Expressions`.
- A power, which has a variable and an exponent, representing expressions such as \\( x^3 \\).

Note we don't want to represent powers of general expressions, such as \\( (4x + 3)^3 \\), as dealing with them will quickly get messy. We also don't have a representation for division and subtraction, because they a bit more complexity than we want for this part of the case study. You can add them back in if you want.

By now you should know how to represent this structure in code, so go ahead and implement it.

In [numerical differentiation](numerical-differentiation.md) we represented mathematical functions as Scala functions. In this section we're representing mathematical functions as Scala data structures. This is an example of a general implementation strategy called *reification*. The abstract meaning of reification is to turn something abstract into something concrete. The concrete meaning, in our case, is to turn functions (or equivalently, methods) into data structures. Reification connects the functional programming world to the object oriented world. We'll see more of this in future.


### Simplifying Expressions

Now we have a representation of expressions we want to simplify them. Instead of, say, \\( x^2 + x^2 \\) we want to have \\( 2x^2 \\). This makes it easier to implement differentiation, which will be our next step.

Conventionally, the types of expressions we're working with are always written from highest power to lowest. So we'd write \\( 4x^2 + 8x + 16 \\), not \\( 8x + 4x^2 + 16 \\). This conventional format is called a *normal form*. Our simplification will convert any expression to its normal form.

To do this we can use various rules of algebra, such as:

- **Associativity:** \\( x + y + z = x + (y + z) \\) and \\( x \times y \times z = x \times (y \times z) \\)
- **Commutivity:** \\( x + y = y + x \\) and \\( x \times y = y \times x \\)
- **Distributivity:** \\( x \times (y + z) = (x \times y) + (y \times z) \\)

Whenever we have an expression involving literals, we can evaluate that expression. This is known as *constant folding* in the compiler literature.

This allows us to transform expressions to find common terms that we can group together. Here are some examples:

- If we have \\( 4 + x \\) we can use commutivity to transform it to \\( x + 4 \\).
- If we have \\( x + 3 + 4 \\) we can use associativity to transform it to \\( x + (4 + 3) \\) and then use constant folding to \\( x + 7 \\)

How should we implement this? Our fundamental tool will be pattern matching, which allows us to look for particular shapes of expressions. For example, we might write a constant folding transform as

```scala
def constantFold(expr: Expression): Expression =
  expr match {
    case Addition(Literal(x), Literal(y)) => Literal(x + y)
    case Multiplication(Literal(x), Literal(y)) => Literal(x * y)
    case other => other
  }

```
