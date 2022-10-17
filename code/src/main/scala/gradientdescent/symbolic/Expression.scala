/*
 * Copyright 2022 Creative Scala
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gradientdescent.symbolic

sealed trait Expression {
  def +(that: Expression): Expression = ???

  def *(that: Expression): Expression = ???

  def bind(variableName: String, value: Double): Expression = ???

  def simplify: Expression = ???

  def differentiate(variableName: String): Expression = ???
}
object Expression {

  /** Create a literal given it's value. */
  def literal(value: Double): Expression = ???

  /** Create a variable given it's name. */
  def variable(name: String): Expression = ???
}
