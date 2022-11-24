package com.cmps.test.sampler

import org.scalatest.flatspec.AnyFlatSpec

import Samplers._
import NonEmptySamplers._

class NonEmptySamplersTest extends AnyFlatSpec {

  "NonEmptySamplers" should "create one element-sized value for all container fields" in {

    val value = "foo"

    implicit val stringSampler: Sampler[String] = instance(value)

    val result = sampleOf[ContainerBean]

    assert(result.opt === Some(value))
    assert(result.seq === Seq(value))
    assert(result.list === List(value))
    assert(result.set === Set(value))
    assert(result.map === Map((value, value)))
  }

}
