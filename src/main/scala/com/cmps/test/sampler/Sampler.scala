/*
 * MIT License
 *
 * Copyright (c) 2022 Carlos Silva
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.cmps.test.sampler

import shapeless._

/**
  * Type class create/obtain values of T
  * @tparam T type of instances to sample
  */
trait Sampler[T] {
  def get: T
}

object Samplers {

  /**
   * Materializes the Sampler for T
   * @param sampler
   * @tparam T
   * @return Sampler[T]
   */
  def apply[T](implicit sampler: Sampler[T]): Sampler[T] = sampler

  /**
   * Creates a Sampler[T] by providing a lazy value (producer)
   * @param value lazy T (producer of T)
   * @tparam T
   * @return Sampler[T] given a lazy T producer
   */
  def instance[T](value: => T): Sampler[T] = new Sampler[T] {
    override def get: T = value
  }

  /**
   * Shortcut method to get a sample of T, without the need to declare or materialize its Sampler
   * @param sampler
   * @tparam T
   * @return
   */
  def sampleOf[T](implicit sampler: Sampler[T]): T = sampler.get

  /**
   * Derives a Sampler for a generic T by using the Sampler for an implementation U
   * @param s
   * @tparam T
   * @tparam U
   * @return Sampler[T] sampling from Sampler[U]
   */
  def select[T,U <: T](implicit s: Sampler[U]): Sampler[T] = new Sampler[T] {
    override def get: T = s.get
  }

  implicit val hnilSampler: Sampler[HNil] = instance(HNil)

  implicit def hlistSampler[H, T <: HList](implicit hs: Sampler[H], ts: Sampler[T]): Sampler[H :: T] =
    instance(hs.get :: ts.get)

  implicit def genericSampler[A, R](implicit gen: Generic.Aux[A, R], sampler: Sampler[R]): Sampler[A] =
    instance(gen.from(sampler.get))

}

