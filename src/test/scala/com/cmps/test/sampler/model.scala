package com.cmps.test.sampler

import enumeratum._
import java.time._

/**
 * EnumEntry to demonstrate sampling of enumeratum enums
 */
sealed trait DogBreed extends EnumEntry

object DogBreed extends Enum[DogBreed] {
  override def values: IndexedSeq[DogBreed] = findValues

  case object Labrador extends DogBreed
  case object Poodle extends DogBreed
  case object Bulldog extends DogBreed
}

/**
 * Sealed trait to demonstrate sampling of ADT sum/coproduct types (polymorphic types)
 */
sealed trait Animal
case class Bird(color: String) extends Animal
case class Dog(breed: DogBreed) extends Animal

/**
 * Case class containing a polymorphic type (animal)
 */
case class PetRegistration(animal: Animal)

/**
 * Case class to demonstrate sampling of numeric fields
 */
case class NumericBean(
               byteValue: Byte,
               shortValue: Short,
               intValue: Int,
               longValue: Long,
               floatValue: Float,
               doubleValue: Double,
               bdValue: BigDecimal,
               biValue: BigInt)

/**
 * Case class to demonstrate sampling of temporal fields
 */
case class TemporalBean(date: LocalDate,
                        dateTime: LocalDateTime,
                        instant: Instant)

/**
 * Case class to demonstrate sampling of other scalar field types
 */
case class ScalarMiscBean(str: String, bool: Boolean)

/**
 * Umbrella/root case class, to demonstrate sampling of ADT product types
 */
case class RootBean(numeric: NumericBean,
                    temporal: TemporalBean,
                    misc: ScalarMiscBean)

/**
 * Case class with diverse container type fields, to demonstrate different ways of sampling containers
 */
case class ContainerBean(
                        opt: Option[String],
                        seq: Seq[String],
                        list: List[String],
                        set: Set[String],
                        map: Map[String, String]
                        )


