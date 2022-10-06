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

package gradientdescent.numerical

import munit._
import cats.syntax.validated

class NumericalDifferentiationSuite extends FunSuite {
  import NumericalDifferentiation._

  def within(actual: Double, expected: Double, epsilon: Double = 0.01)(implicit
      loc: Location
  ): Unit = {
    val diff = Math.abs(actual - expected)
    if diff > epsilon then
      fail(s"$actual was more than $epsilon from $expected")
    else ()
  }

  test("Numerical differentiation returns exact answer for lines") {
    val f1: Double => Double = x => x
    val df1 = differentiate(0.01)(f1)
    val f2: Double => Double = x => 2 * x
    val df2 = differentiate(0.01)(f2)

    assertEquals(df1(0), 1.0)
    assertEquals(df2(0), 2.0)
  }

  test("Numerical differentiation is approximately correct for sine") {
    val f: Double => Double = x => Math.sin(x)
    val df = differentiate(0.01)(f)

    within(df(0.0), Math.cos(0.0))
    within(df(1.0), Math.cos(1.0))
  }

}
