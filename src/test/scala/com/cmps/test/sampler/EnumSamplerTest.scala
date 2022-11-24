package com.cmps.test.sampler

import org.scalatest.flatspec.AnyFlatSpec

class EnumSamplerTest extends AnyFlatSpec with SamplerTest {

  "FixedSamplers" should "always pick the 1st value of the Enumerated" in {

    import FixedSamplers._

    val all = thousandOf[DogBreed]
    assertUniqueCount(all, 1)
    assert(all.head === DogBreed.Labrador)
  }

  "UniqueSamplers" should "round robin pick among all values of the Enumerated" in {

    import UniqueSamplers._

    assertUniqueCount(thousandOf[DogBreed], DogBreed.values.size)
  }

}
