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

package gradientdescent.utils

import doodle.core.Point
import doodle.algebra.{Path, Picture}
import doodle.syntax.all._
import doodle.core.BoundingBox

object Plot {

  /** Plot a function from xStart to xEnd. Use the scale function to transform
    * from the function coordinates to the screen coordinates.
    */
  def apply[Alg[x[_]] <: Path[x], F[_]](
      f: Double => Double,
      xStart: Double,
      xEnd: Double,
      scale: Point => Point = identity
  ): Picture[Alg, F, Unit] = {
    val x0 = xStart.min(xEnd)
    val x1 = xStart.max(xEnd)
    val range = x1 - x0

    val pts =
      for (x <- 0.to(100).map(x => (x.toDouble / 100.0) * range + x0))
        yield scale(Point(x, f(x)))

    interpolatingSpline[Alg, F](pts.toList)
  }

  def function[Alg[x[_]] <: Path[x], F[_]](x: Int, y: Int)(
      xStart: Double,
      xEnd: Double
  )(
      f: Double => Double
  ): Picture[Alg, F, Unit] = {
    val x0 = xStart.min(xEnd)
    val x1 = xStart.max(xEnd)
    val range = x1 - x0

    val pts =
      (for (x <- 0.to(100).map(x => (x.toDouble / 100.0) * range + x0))
        yield Point(x, f(x))).toList

    val (left, top, right, bottom) = {
      val first = pts.head
      pts.foldLeft((first.x, first.y, first.x, first.y)) { (accum, pt) =>
        val (left, top, right, bottom) = accum

        (left.min(pt.x), top.max(pt.y), right.max(pt.x), bottom.min(pt.y))
      }
    }

    val xLeft = -x.toDouble / 2.0
    val yBottom = -y.toDouble / 2.0

    // Map the center of the data to the origin
    val xOffset = (x1 - x0) / 2 + x0
    val yOffset = (top - bottom) / 2 + bottom
    val xScale = x.toDouble / (right - left)
    val yScale = y.toDouble / (top - bottom)

    val scaledPts =
      pts.map(pt => Point((pt.x + xOffset) * xScale, (pt.y + yOffset) * yScale))

    println(s"bb1: $left $top $right $bottom")
    println(scaledPts.foldLeft(BoundingBox.empty)((bb, pt) => bb.enclose(pt)))
    println(s"x $xOffset : $xScale")
    println(s"y $yOffset : $yScale")

    interpolatingSpline[Alg, F](scaledPts)
  }

  def linearInterpolation(xScale: Double, yScale: Double): Point => Point =
    pt => Point(pt.x * xScale, pt.y * yScale)
}
