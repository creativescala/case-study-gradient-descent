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

final case class GradientDescent(learningRate: Double, df: Double => Double) {

  /** Perform one iteration of gradient descent, using numerical
    * differentiation.
    */
  def iterate(x: Double): Double = {
    val dx = df(x)

    x - (learningRate * dx)
  }

  def gradientDescent(x: Double, iterations: Int): Double =
    if (iterations == 0) x
    else gradientDescent(iterate(x), iterations - 1)
}
