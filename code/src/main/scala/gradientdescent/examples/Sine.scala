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

import gradientdescent.utils.Plot
import doodle.svg._
import doodle.syntax.all._
import doodle.explore.laminar.Explore
import scala.scalajs.js.annotation._
import cats.effect.unsafe.implicits.global

@JSExportTopLevel("Sine")
object Sine {
  import Explore.given
  // a = 100
  val basicSine: Double => Double => Double = a => x => a * Math.sin(x)

  def basicPlot(f: Double => Double): Picture[Unit] =
    Plot(
      f,
      -6.0,
      6.0,
      Plot.linearInterpolation(50.0, 1.0)
    )

  @JSExport
  def drawBasicPlot(id: String): Unit = {
    val frame = Frame(id).size(600, 200)

    Explore
      .int("a")
      .within(-100, 100)
      .withDefault(50)
      .explore(frame)(a => basicPlot(basicSine(a.toDouble)))
  }
}
