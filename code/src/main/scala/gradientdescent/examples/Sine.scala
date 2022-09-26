package gradientdescent.examples

import gradientdescent.utils.Plot
import doodle.svg._
import doodle.syntax.all._
import scala.scalajs.js.annotation._
import cats.effect.unsafe.implicits.global

@JSExportTopLevel("Sine")
object Sine {
  // a = 100
  val basicSine: Double => Double = x => 100 * Math.sin(x)

  val basicPlot: Picture[Unit] =
    Plot(
      basicSine,
      -3.0,
      3.0,
      Plot.linearInterpolation(100.0, 1.0)
    )

  @JSExport
  def drawBasicPlot(id: String) =
    basicPlot.drawWithFrame(Frame(id).size(600, 200))
}
