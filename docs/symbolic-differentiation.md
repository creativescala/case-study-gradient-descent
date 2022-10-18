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

We're going to work with fairly simple expressions, like \\( x^2 + 4 \\) and \\( ax + 2x + 8 \\).

These expressions consist of

- numbers, like \\(2\\) and \\(8\\);
- variables, like \\(a\\) and \\(x\\);
- addition and multiplication.

We're going to represent expressions as follows. An `Expression` is one of:

- A literal number, containing a `Double` value.
- A variable, containing a `String` name, which represents a variable such as \\( x \\) or \\( a \\) in our function. 
- The addition of two `Expressions`.
- The multiplication of two `Expressions`.

By now you should know how to represent this structure in code, so go ahead and implement it. There is a skeleton in `Expression.scala` to get you started.


## Differentiating Expressions

With a representation of expressions we can go ahead and implement differentiation. Implement the method `differentiate` on `Expression`. This is perhaps the most complex task here, so you might choose to tackle the tasks below before returning to differentiation.


## Binding Variables

Our expressions contains variables, such as \\(a\\) and \\(x\\) in \\( ax + 2x + 8 \\). When a variable hasn't taken on a particular value we call it a *free variable*. It's free in that it can take on any value.

We need to be able to give values to variables. For example, when evaluating a derivative at a particular point we might want to say that \\(x\\) is \\(1\\) in \\( 8x + 8 \\). Variables that have taken on a fixed value are called *bound variables*, and hence we say we bind a variable to a value.

Note that binding a variable to a value in an expression still leaves us with an expression, not necessarily a number as you may expect. Considering binding \\(x\\) to \\(1\\) in \\( ax + 2x + 8 \\). We're left with \\( (a \times 1) + (2 \times 1) + 8 \\).

Binding is a form of substitution. We substitute a value for every occurrence of a variable. Implement a method `bind` that accepts both a variable name (a `String`) and a value for that variable (a `Double`), and substitutes the value for all occurrences of the variable within the `Expression`.


## Simplifying Expressions

After binding a variable to a value we'll usually end up with a lot of constant expressions: multiplications or additions that involve only literals. We saw that in the example above where binding \\(x\\) to \\(1\\) in \\( ax + 2x + 8 \\) left us with \\( (a \times 1) + (2 \times 1) + 8 \\). Idealy we would want just \\( a + 2 + 8 \\) or even just \\( a + 10 \\).

To do this we want to implement a method to *simplify* expressions. This, like binding a variable, is an `Expression` to `Expression` transform. Simplification is as complex a problem as we care to make it. In this instance we're going to keep it very simple: we just want to simplify additions and multiplications of literals. So if we have \\( x + (1 + 2) \\) we should end up \\( x + 3 \\), but if we have \\( (x + 1) + 2 \\) we won't make any change.

Using our very simple approach to simplification means it may be possible to simplify an expression a multiple times. For example, if we have the expression \\( (1 + 2) + 3 \\), the first simplification should give us \\( 3 + 3 \\), which we can then simplify again to reach \\( 6 \\). Once we have reached \\( 6 \\) no further simplifications are possible.

Our simple simplification algorithm misses many opportunities for simplification. For example, it won't simplify \\( (3 + x) + 5 \\) to \\( x + 8 \\). However it does have one very nice property, which is that performing simplification either returns a simpler, shorter, expression or we make no change and the result is the expression we started with. This means our simplification is guaranteed to eventually reach a point where no further simplification can occur. In formal terms we can say repeated application of simplification always converges to a *fixed point*. A fixed point of a function \\( f \\) is a value \\( x \\) such that \\(f(x) = x \\). For example \\(\\sin(0) = 0\\), so \\(0\\) is a fixed point of \\(\\sin\\).

Now go ahead an implement `simplify`. You may choose to implement the basic simplification we described above, or to iterate the basic algorithm until you reach a fixed point.


## Gradient Descent

With all the above in place we have everything we need to differentiate an expression and calculate the gradient at a particular point. This in turn means we can implement gradient descent.

We don't have the \\(\sin\\) function (and it's derivative, \\(\\cos\\)) in `Expression`. You could add it, if you wanted, but it's simpler to use a different function that we can represent in `Expression`. For example, we could use a cubic function \\(f(x, a) = ax^3 \\). There is cubic data in `Data.scala`. The example below shows 40 points randomly sampled from a cubic function, with added noise, and plots it against a cubic for which you can vary the parameter \\(a\\).

@:doodle(draw-cubic-plot, Cubic.drawCubicPlot)

It can also be interesting to try gradient descent on the \\(\sin\\) data, where the model is cubic. Here the model cannot make a good approximation of the data, but gradient descent will still find something.


## Complex Simplification

As an extension, you could look at more complex rules for simplification. These rules allow more simplifications to occur, at the cost of turning the problem into one of searching amongst multiple possible solutions. Let's look at a few examples to see what patterns emerge.

If we have \\( (x + 3) + 2 \\) we may want to simplify it to \\( x + 5 \\). To get there we need an intermediate step, which is to rewrite \\( (x + 3) + 2 \\) to \\( x + (3 + 2) \\).

If we have \\( (3 + x) + 2 \\), and our goal is \\( x + 5 \\), we can use the following two transformations:

1. transform \\( (3 + x) + 2 \\) to \\( (x + 3) + 2 \\); and
2. transform \\( (x + 3) + 2 \\) to \\( x + (3 + 2) \\).

If we have \\( 2 (3 + x) + x \\), we may want to simplify to \\( 3x + 6 \\). To achieve this we can:

1. multiply out to arrive at \\( 6 + 2x + x \\); and
2. simplify to 6 + 3x.

The above examples made use of rules known as associativity, commutivity, and distributivity. They are:

1. *Associativity*: \\( (x + y) + z = x + (y + z) \\), and similarly for expressions involving multiplication.
2. *Commutivity*: \\( x + y = y + x \\), and similarly for expressions involving multiplication.
3. *Distributivity*: \\( a (x + y) = ax + ay \\).

We can use these rules to generate new expressions that we can then attempt to simplify, and we can keep the result that is the shortest. For example, if we have an expression \\( x + y \\), we can use commutivity to create \\( y + x \\). We can then simplify both \\( x + y \\) and \\( y + x \\) and see which result is shorter.


## Summary and Context

There is a lot going on in this part of the case study. Here are some the main ideas, and connections to other areas in computer science.

The main programming tools we're using are algebraic data types and structural recursion. You'll probably have seen some pattern matches that go a bit beyond what you might have seen before. Learning a bit more about what you can do with pattern matching is a good exercise; it's a powerful tool!

Moving beyond pattern matching, the idea of manipulating a data structure that represents a program (in our case, an arithmetic expression) is a very powerful one. The key idea of a compiler, like the Scala compiler, is to represent programs as a data structure, and then manipulate that data structure to achieve various goals so as improving performance (which is equivalent to simplification) or producing a different kind of program (such as compiling Scala code into JVM bytecode.) Working compilers are a lot more complex than `Expression`, but the core ideas are exactly the same.

We can also view `Expression` as a *rewrite system*. Rewrite systems consist of rules that match input and convert them into output. A simple example of a rewrite is the power rule for differentiation. Recall, it is

$$ \textrm{If} \; f(x) = x^n \; \textrm{then} \; \frac{df(x)}{dx} = nx^{n-1} $$

We can view this as a rewrite. It says that whenever the input is \\(x^n\\) we write \\(nx^{n-1}\\) as the output.

Rewrite rules are closely connected to the theory of programing languages, and to programs that reason about symbolic systems, such as computer algebra systems that help people do mathematics.
