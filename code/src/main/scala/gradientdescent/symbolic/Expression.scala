package gradientdescent.symbolic

sealed trait Expression {
  def +(that: Expression): Expression = ???

  def *(that: Expression): Expression = ???

  def apply(parameterName: String, value: Double): Expression = ???

  def simplify: Expression = ???

  def differentiate(parameterName: String): Expression = ???
}
