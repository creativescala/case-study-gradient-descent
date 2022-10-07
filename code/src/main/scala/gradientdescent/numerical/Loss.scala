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

import doodle.core.Point

object Loss {

  /** Given data and a function of two parameters, create the loss function. The
    * first parameter is x, the data location. The second parameter is the
    * parameter being optimized. we're optimizing.
    */
  def loss(
      data: List[Point]
  )(f: (Double, Double) => Double): Double => Double = { (a: Double) =>
    gradientdescent.Loss.loss(data)(x => f(x, a))
  }
}
