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

package gradientdescent.numerical

import doodle.core._
import doodle.syntax.all._
import doodle.interact.animation._
import doodle.interact.syntax.all._
import doodle.java2d._
import cats.effect.unsafe.implicits.global

import gradientdescent.Plot

object Animation {
  val data = gradientdescent.Data.sineData
  val f: (Double, Double) => Double = (x, a) => a * Math.sin(x)
  val scale = Plot.linearInterpolation(50.0, 1.0)
  val loss: Double => Double =
    ??? // Create the loss function here, given f and data above

  def animation(initial: Double, iterations: Int): Transducer[Picture[Unit]] = {
    val gd = GradientDescent(0.01)(???)
    Transducer
      .fromList(List.range(0, iterations))
      .scanLeft(initial) { (a, _) =>
        val updatedX = gd.iterate(a)(loss)
        updatedX
      }
      .map { a =>
        Plot
          .data(scale)(data)
          .on(Plot.function(-6.0, 6.0, scale)(x => f(x, a)))
      }
  }

  animation(10.0, 1000).animate(Frame.size(600, 600))
}
