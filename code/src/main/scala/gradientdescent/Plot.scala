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

import doodle.core._
import doodle.algebra._
import doodle.syntax.all._

object Plot {

  /** Plot data points. Use the scale function to transform from the data
    * coordinates to screen coordinates
    */
  def data[Alg[x[_]] <: Layout[x] & Shape[x] & Style[x], F[_]](
      scale: Point => Point = identity
  )(data: List[Point]): Picture[Alg, F, Unit] = {
    data.map(pt => circle[Alg, F](5).fillColor(Color.blue).at(scale(pt))).allOn
  }

  /** Plot a function from xStart to xEnd. Use the scale function to transform
    * from the function coordinates to the screen coordinates.
    */
  def function[Alg[x[_]] <: Path[x], F[_]](
      xStart: Double,
      xEnd: Double,
      scale: Point => Point = identity
  )(f: Double => Double): Picture[Alg, F, Unit] = {
    val x0 = xStart.min(xEnd)
    val x1 = xStart.max(xEnd)
    val range = x1 - x0

    val pts =
      for (x <- 0.to(100).map(x => (x.toDouble / 100.0) * range + x0))
        yield scale(Point(x, f(x)))

    interpolatingSpline[Alg, F](pts.toList)
  }

  def linearInterpolation(xScale: Double, yScale: Double): Point => Point =
    pt => Point(pt.x * xScale, pt.y * yScale)
}
