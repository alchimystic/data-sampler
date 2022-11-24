package com.cmps.test.sampler

import org.scalatest.flatspec.AnyFlatSpec
import Samplers._
import UniqueSamplers._

class UniqueSamplersTest extends AnyFlatSpec with SamplerTest {

  "UniqueSamplers" should "create unique complex values" in {
    val list = thousandOf[RootBean]
    val set = list.toSet
    assert(set.size === list.size) // all samples are unique
  }

  it should "create unique Bytes" in {
    assertUniqueCount(thousandOf[Byte], 256) // only 256 unique byte values
  }

  it should "create unique Shorts" in {
    assertUniqueCount(thousandOf[Short])
  }

  it should "create unique Ints" in {
    assertUniqueCount(thousandOf[Int])
  }

  it should "create unique Longs" in {
    assertUniqueCount(thousandOf[Long])
  }

  it should "create unique Floats" in {
    assertUniqueCount(thousandOf[Float])
  }

  it should "create unique Doubles" in {
    assertUniqueCount(thousandOf[Double])
  }

  it should "create unique BigDecimals" in {
    assertUniqueCount(thousandOf[BigDecimal])
  }

  it should "create unique BigInts" in {
    assertUniqueCount(thousandOf[BigInt])
  }

  it should "create unique Strings" in {
    assertUniqueCount(thousandOf[String])
  }

  it should "create unique Booleans" in {
    assertUniqueCount(thousandOf[Boolean], 2)
  }

  it should "pick unique Enumerateds" in {
    assertUniqueCount(thousandOf[DogBreed], DogBreed.values.size)
  }

}
