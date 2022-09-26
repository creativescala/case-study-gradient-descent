package gradientdescent.utils

import doodle.core.Point
import doodle.algebra.{Path, Picture}
import doodle.syntax.all._

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

  def linearInterpolation(xScale: Double, yScale: Double): Point => Point =
    pt => Point(pt.x * xScale, pt.y * yScale)
}
