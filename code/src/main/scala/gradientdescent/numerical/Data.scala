package gradientdescent.numerical

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

  /** Example data set for f(x) = 50 sin(x) */
  val sineData =
    create(40, -6.0, 6.0, 5.0)(x => 50 * Math.sin(x))
}
