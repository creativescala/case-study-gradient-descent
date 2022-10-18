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

package gradientdescent.examples

import gradientdescent.utils._
import doodle.core._
import doodle.svg._
import doodle.syntax.all._
import doodle.explore.laminar.Explore
import scala.scalajs.js.annotation._
import cats.effect.unsafe.implicits.global
import scala.util.Random

@JSExportTopLevel("Cubic")
object Cubic {
  import Explore.given
  val basicCubic: Double => Double => Double = a => x => a * Math.pow(x, 3.0)

  val trueCubic: Double => Double = basicCubic(0.33)

  val data =
    (1.to(40)).map { _ =>
      val x = Random.between(-6.0, 6.0)
      val y = trueCubic(x) + (5.0 * Random.nextGaussian())
      Point(x * 50.0, y)
    }

  def basicPlot(f: Double => Double): Picture[Unit] =
    Plot(
      f,
      -6.0,
      6.0,
      Plot.linearInterpolation(50.0, 0.5)
    )

  def dataPlot(data: Seq[Point]): Picture[Unit] =
    data
      .map(pt => Picture.circle(5.0).fillColor(Color.blue).noStroke.at(pt))
      .allOn

  @JSExport
  def drawCubicPlot(id: String): Unit = {
    val frame = Frame(id).size(640, 240)
    Explore
      .int("a")
      .within(-100, 100)
      .withDefault(0)
      .explore(frame)(a =>
        dataPlot(data).on(basicPlot(basicCubic(a.toDouble / 100.0)))
      )
  }
}
