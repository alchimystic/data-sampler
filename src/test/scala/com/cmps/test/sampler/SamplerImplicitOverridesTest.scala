package com.cmps.test.sampler

import org.scalatest.flatspec.AnyFlatSpec

import Samplers._
import FixedSamplers._ // this import should be kept to prove it will be overridden
import EmptySamplers._

class SamplerImplicitOverridesTest extends AnyFlatSpec {

  "Implicit Samplers" should "be overridden when needed" in {

    val stringValue = "foo"

    def listOf[T](value1: T, value2: T): List[T] = List(value1, value2)

    // overriding sampler for String
    implicit val stringSampler: Sampler[String] = instance(stringValue)

    // overriding sampler for List[T]
    implicit def listSampler[T:Sampler]: Sampler[List[T]] = instance(listOf(sampleOf[T], sampleOf[T]))

    val result = sampleOf[ContainerBean]

    assert(result.list === listOf(stringValue, stringValue)) // list and string samplers were overridden
    assert(result.seq.size === 0) // empty seq sampler was kept
  }

}
