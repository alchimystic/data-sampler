package com.cmps.test.sampler

import org.scalatest.flatspec.AnyFlatSpec

import Samplers._
import FixedSamplers._

class FixedSamplersTest extends AnyFlatSpec {

  "FixedSamplers" should "create predictable values" in {

    val expectedNumber = FixedSamplers.numericValue.toDouble
    val expectedInstant = FixedSamplers.temporalValue
    val expectedBoolean = FixedSamplers.booleanValue
    val expectedString = FixedSamplers.stringValue

    val result = sampleOf[RootBean]

    assert(result.numeric.byteValue === expectedNumber)
    assert(result.numeric.shortValue === expectedNumber)
    assert(result.numeric.intValue === expectedNumber)
    assert(result.numeric.longValue === expectedNumber)
    assert(result.numeric.floatValue === expectedNumber)
    assert(result.numeric.doubleValue === expectedNumber)
    assert(result.numeric.bdValue === expectedNumber)
    assert(result.numeric.biValue === expectedNumber)

    assert(result.temporal.date === toLocalDate(expectedInstant))
    assert(result.temporal.dateTime === toLocalDateTime(expectedInstant))
    assert(result.temporal.instant === expectedInstant)

    assert(result.misc.str === expectedString)
    assert(result.misc.bool === expectedBoolean)

    assert(sampleOf[RootBean] === result) //another sample will hold the same values
  }

}
