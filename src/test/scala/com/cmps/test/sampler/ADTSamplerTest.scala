package com.cmps.test.sampler

import com.cmps.test.sampler.FixedSamplers._
import com.cmps.test.sampler.Samplers._
import org.scalatest.flatspec.AnyFlatSpec

class ADTSamplerTest extends AnyFlatSpec {

  "Samplers" should "be able to pickup a sealed trait sample by selecting the concrete class" in {

    def getBirdRegistration: PetRegistration = {
      implicit val animalSampler = select[Animal, Bird]
      sampleOf[PetRegistration]
    }

    def getDogRegistration: PetRegistration = {
      implicit val animalSampler = select[Animal, Dog]
      sampleOf[PetRegistration]
    }

    assert(getBirdRegistration.animal.isInstanceOf[Bird] == true)

    assert(getDogRegistration.animal.isInstanceOf[Dog] == true)
  }
}
