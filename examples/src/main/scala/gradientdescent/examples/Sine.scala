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

@JSExportTopLevel("Sine")
object Sine {
  import Explore.given
  val basicSine: Double => Double => Double = a => x => a * Math.sin(x)

  def basicPlot(f: Double => Double): Picture[Unit] =
    Plot(
      f,
      -6.0,
      6.0,
      Plot.linearInterpolation(50.0, 1.0)
    )

  def dataPlot(data: Seq[Point]): Picture[Unit] =
    data
      .map(pt => Picture.circle(5.0).fillColor(Color.blue).noStroke.at(pt))
      .allOn

  def lossBarsPlot(data: Seq[Point])(f: Double => Double): Picture[Unit] = {
    Picture
      .path(
        data
          .map(pt => OpenPath.empty.moveTo(pt).lineTo(pt.x, f(pt.x / 50.0)))
          .fold(OpenPath.empty)(_.append(_))
      )
      .strokeColor(Color.blue)
  }

  @JSExport
  def drawBasicPlot(id: String): Unit = {
    val frame = Frame(id).size(640, 240)

    Explore
      .int("a")
      .within(-100, 100)
      .withDefault(50)
      .explore(frame)(a => basicPlot(basicSine(a.toDouble)))
  }

  @JSExport
  def drawErrorPlot(id: String): Unit = {
    val trueF = basicSine(75)
    val data = (1.to(40)).map { _ =>
      val x = Random.between(-6.0, 6.0)
      val y = trueF(x) + (5.0 * Random.nextGaussian())
      Point(x * 50.0, y)
    }

    val frame = Frame(id).size(640, 240)
    Explore
      .int("a")
      .within(-100, 100)
      .withDefault(50)
      .explore(frame)(a => dataPlot(data).on(basicPlot(basicSine(a.toDouble))))
  }

  @JSExport
  def drawLossPlot(id: String): Unit = {
    val trueF = basicSine(75)
    val data = (1.to(40)).map { _ =>
      val x = Random.between(-6.0, 6.0)
      val y = trueF(x) + (5.0 * Random.nextGaussian())
      Point(x * 50.0, y)
    }

    val frame = Frame(id).size(640, 240)
    Explore
      .int("a")
      .within(-100, 100)
      .withDefault(50)
      .explore(frame) { a =>
        val model = basicSine(a.toDouble)
        val loss = Loss.loss(data.toList)(x => model(x / 50.0))

        dataPlot(data)
          .on(lossBarsPlot(data)(model))
          .on(basicPlot(model))
          .on(text("Loss: %.2f".format(loss)).fillColor(Color.black))
      }
  }

  @JSExport
  def drawNumericalDifferentiationPlot(id: String): Unit = {
    val f = basicSine(75)
    def gradient(h: Double): Double =
      (f(0) + f(h)) / h

    val frame = Frame(id).size(640, 240)
    Explore
      .int("h")
      .within(-100, 100)
      .withDefault(10)
      .explore(frame) { h =>
        val x = h.toDouble / 50.0
        val g = gradient(x)
        val y = f(x)
        val zeroY = f(0)
        val hPt = Picture.circle(10).fillColor(Color.blue).at(h, y)
        val zeroPt = Picture.circle(10).fillColor(Color.black).at(0, zeroY)
        val pt = zeroPt.on(hPt)

        val line = Picture
          .path(
            ClosedPath.empty
              .moveTo(
                if h < -50 then h else -50,
                if h < -50 then zeroY + (g * x) else zeroY - g
              )
              .lineTo(
                if h > 50 then h else 50,
                if h > 50 then zeroY + (g * x) else zeroY + g
              )
          )
          .strokeWidth(2.0)
        pt.on(line).on(basicPlot(f))
      }
  }
}
