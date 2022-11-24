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

import com.cmps.test.sampler.Samplers.instance

/**
 * Samplers for empty containers (no values)
 */
object EmptySamplers {

  implicit def optionSampler[T]: Sampler[Option[T]] = instance(None)
  implicit def seqSampler[T]: Sampler[Seq[T]] = instance(Nil)
  implicit def listSampler[T]: Sampler[List[T]] = instance(Nil)
  implicit def setSampler[T]: Sampler[Set[T]] = instance(Set.empty)
  implicit def mapSampler[K, V]: Sampler[Map[K, V]] = instance(Map.empty)
}

/**
 * Samplers for containers with one element
 */
object NonEmptySamplers {

  implicit def optionSampler[T](implicit sampler: Sampler[T]): Sampler[Option[T]] = instance(Some(sampler.get))
  implicit def seqSampler[T](implicit sampler: Sampler[T]): Sampler[Seq[T]] = instance(Seq(sampler.get))
  implicit def listSampler[T](implicit sampler: Sampler[T]): Sampler[List[T]] = instance(List(sampler.get))
  implicit def setSampler[T](implicit sampler: Sampler[T]): Sampler[Set[T]] = instance(Set(sampler.get))
  implicit def mapSampler[K, V](implicit ks: Sampler[K], vs: Sampler[V]): Sampler[Map[K, V]] =
    instance(Map(ks.get -> vs.get))
}
