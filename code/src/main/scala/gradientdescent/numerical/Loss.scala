package gradientdescent.numerical

import doodle.core.Point

object Loss {

  /** Calculated squared error / quadratic loss on a particular data point. */
  def pointLoss(pt: Point)(f: Double => Double): Double = {
    val expected = pt.y
    val actual = f(pt.x)
    val error = (actual - expected)

    error * error
  }

  /** Calculate squared error / quadratic loss on a data set. */
  def loss(data: List[Point])(f: Double => Double) = {
    data.foldLeft(0.0) { (accum, pt) => pointLoss(pt)(f) }
  }
}
