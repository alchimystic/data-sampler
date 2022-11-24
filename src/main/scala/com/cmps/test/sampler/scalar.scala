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
import enumeratum.EnumEntry

import java.time._
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong
import scala.util.Random

trait CoreSamplerSet {

  def numericValue: Int
  def temporalValue: Instant
  def stringValue: String
  def booleanValue: Boolean
  def valueFrom[T](list: List[T]): T

  private def zone = ZoneId.systemDefault()

  implicit def byteSampler: Sampler[Byte] = instance(numericValue.toByte)
  implicit def shortSampler: Sampler[Short] = instance(numericValue.toShort)
  implicit def intSampler: Sampler[Int] = instance(numericValue)
  implicit def longSampler: Sampler[Long] = instance(numericValue.toLong)
  implicit def floatSampler: Sampler[Float] = instance(numericValue.toFloat)
  implicit def doubleSampler: Sampler[Double] = instance(numericValue.toDouble)
  implicit def bigDecimalSampler: Sampler[BigDecimal] = instance(BigDecimal.int2bigDecimal(numericValue))
  implicit def bigIntSampler: Sampler[BigInt] = instance(BigInt.int2bigInt(numericValue))

  implicit def localDateSampler: Sampler[LocalDate] = instance(toLocalDate(temporalValue))
  implicit def localDateTimeSampler: Sampler[LocalDateTime] = instance(toLocalDateTime(temporalValue))
  implicit def instantSampler: Sampler[Instant] = instance(temporalValue)

  implicit def boolSampler: Sampler[Boolean] = instance(booleanValue)
  implicit def stringSampler: Sampler[String] = instance(stringValue)

  implicit def enumeratedSampler[T <: EnumEntry](implicit en: Enumerated[T]): Sampler[T] =
    instance(valueFrom(en.allValues))

  def toLocalDate(value: Instant): LocalDate = LocalDate.ofInstant(value, zone)
  def toLocalDateTime(value: Instant): LocalDateTime = LocalDateTime.ofInstant(value, zone)

}

/**
 * CoreSamplerSet impl with fixed values
 */
object FixedSamplers extends CoreSamplerSet {

  private lazy val fixedInstant: Instant = Instant.now()

  override val numericValue: Int = 1
  override def temporalValue: Instant = fixedInstant
  override def stringValue: String = "str"
  override def booleanValue: Boolean = false
  override def valueFrom[T](list: List[T]): T = list.head // picks up the 1st value, whatever that is
}

/**
 * CoreSamplerSet impl with unique (as much as possible) values
 */
object UniqueSamplers extends CoreSamplerSet {

  private val counter = new AtomicLong(0) // counter used to produce unique numerics
  private val random = new Random() // random used to produce variation on temporal values
  private val minutesInMonth = 60 * 24 * 30

  override def numericValue: Int = counter.incrementAndGet().toInt

  // random instant in the last month up until now
  override def temporalValue: Instant = Instant.now().minus(Duration.ofMinutes(random.between(0, minutesInMonth)))

  override def stringValue: String = "s" + nextId

  override def booleanValue: Boolean = nextId % 2 == 0 // round robin on booleans

  override def valueFrom[T](list: List[T]): T = list(nextId.toInt % list.size) // round robin on list elements

  private val uniqueIdSampler: Sampler[Long] = instance(counter.incrementAndGet())
  private val uuidSampler: Sampler[String] = instance(UUID.randomUUID().toString)

  def nextId: Long = uniqueIdSampler.get

  // string based on uuid
  def someUuid: String = uuidSampler.get

  // string based on truncated uuid (less likely to be unique)
  def someString: String = uuidSampler.get.substring(0, 8)

}
