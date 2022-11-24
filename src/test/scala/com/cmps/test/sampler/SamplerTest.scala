package com.cmps.test.sampler

import org.scalatest.flatspec.AnyFlatSpec
import Samplers._

trait SamplerTest {

  this: AnyFlatSpec => // self-type: anything implementing SamplerTest must also extend AnyFlatSpec

  private val thousand = 1000

  def assertUniqueCount[T](list: List[T], expectedCount: Int = thousand): Unit = {
    assert(list.toSet.size === expectedCount)
  }

  def thousandOf[T:Sampler]: List[T] = Range.inclusive(1, thousand).toList.map(i => sampleOf[T])

}
