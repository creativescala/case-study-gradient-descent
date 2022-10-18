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

package gradientdescent

import doodle.core.Point
import scala.util.Random

object Data {

  /** Create a data set consisting of count elements, each element has an x
    * value between xStart and xStop, and a y value taken from f plus gaussian
    * noise with the given variance.
    */
  def create(count: Int, xStart: Double, xStop: Double, variance: Double)(
      f: Double => Double
  ): List[Point] =
    List.fill(count) {
      val x = Random.between(xStart, xStop)
      val noise = Random.nextGaussian() * variance
      val y = f(x) + noise

      Point(x, y)
    }

  /** Example data set for f(x) = 75 sin(x) */
  val sineData =
    create(40, -6.0, 6.0, 5.0)(x => 75 * Math.sin(x))

  /** Example data set for f(x) = 0.25 x^3 */
  val cubicData =
    create(40, -6.0, 6.0, 5.0)(x => 0.25 * Math.pow(x, 3.0))
}
