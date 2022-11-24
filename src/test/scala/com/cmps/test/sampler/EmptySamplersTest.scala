package com.cmps.test.sampler

import org.scalatest.flatspec.AnyFlatSpec
import Samplers._
import EmptySamplers._

class EmptySamplersTest extends AnyFlatSpec {

  "EmptySamplers" should "create empty values for all container fields" in {

    val result = sampleOf[ContainerBean]

    assert(result.opt.size === 0)
    assert(result.seq.size === 0)
    assert(result.list.size === 0)
    assert(result.set.size === 0)
    assert(result.map.size === 0)
  }

}
