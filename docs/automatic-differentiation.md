# Automatic Differentiation

We'll now look at our final method for calculating derivatives: automatic differentiation. We'll introduce it with a bit of a digression into the history of calculus.

In the section on numerical differentiation we said we can approximately calculate the gradient with the following equation:

$$ gradient \approx \frac{f(x + h) - f(x)}{h} $$

We can work with this symbolically to calculate derivates. Let's try \\(f(x) = x^2 \\). We know from the section on symbolic differentiation that \\(\frac{df(x)}{dx} = 2x \\). Let's see what happens when we substitute \\(f(x) = x^2 \\) into the definition of the gradient above.

$$
\begin{align}
\frac{df(x)}{dx} & = \frac{f(x + h) - f(x)}{h} \\
& = \frac{(x + h)^2 - x^2}{h} \\
& = \frac{x^2 + 2xh + h^2 - x^2}{h} \\
& = \frac{2xh + h^2}{h} \\
& = \frac{h(2x + h)}{h} \\
& = 2x + h
\end{align}
$$

Now if we make \\(h\\) really really small we can just ignore it and end up with 

$$ \frac{df(x)}{dx} = 2x $$

This is the answer we expect, but we've done some slightly dodgy mathematics to arrive at it. The problem is with how we treat \\(h\\). When we said we can ignore \\(h\\) we're saying that \\(h = 0\\). However, in a prior step we had \\(\frac{h}{h}\\) which we said cancelled out. This works so long as \\(h \neq 0\\), otherwise we end up with \\(\frac{0}{0}\\) which is not defined. Effectively we're saying that \\(h\\) is infinitesimally small: so small we can ignore it when we want but still not zero so that \\(\frac{h}{h}\\) is defined.

This type of algebra was used when calculus was first invented, but the issues with it led to the replacement with what is now the standard approach to precisely defining calculus. This approach is known as *standard analysis* and it uses limits instead of infinitesimals.

However, around the 1960s the concept of infinitesimals were precisely defined, leading to a new way of defining calculus. This is known as [nonstandard analysis](https://en.wikipedia.org/wiki/Nonstandard_analysis).
