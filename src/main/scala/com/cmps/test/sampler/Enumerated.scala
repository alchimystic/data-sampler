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

import enumeratum.EnumEntry

/**
  * Type class to obtain all the values for some type T
  * @tparam T some type (not necessarily an enum)
  */
trait Enumerated[T] {
  def allValues: List[T]
}

object Enumerated {
  def apply[T](implicit en: Enumerated[T]): Enumerated[T] = en

  def instance[T](values: => List[T]): Enumerated[T] = new Enumerated[T] {
    override def allValues: List[T] = values
  }

  import scala.reflect.runtime.{universe => ru}

  /**
    * Ikplicit to get an Enumerated where T is an EnumEntry
    *
    * @param tag
    * @tparam T
    * @return
    */
  implicit def enumeratedEnumEntry[T <: EnumEntry](implicit tag: ru.TypeTag[T]): Enumerated[T] =
    instance(getAllEnumEntryValues)

  /**
    * Method to get EnumEntries, using the companion object extending Enum[E], if exists
    * @param tag
    * @tparam E
    * @return enum entries in the order they are defined in the companion object
    */
  private def getAllEnumEntryValues[E <: EnumEntry](implicit tag: ru.TypeTag[E]): List[E] = {
    val clazz = tag.tpe.typeSymbol.asClass
    val targetEnumClass = classOf[enumeratum.Enum[E]]
    val classLoader = this.getClass.getClassLoader // this is the most likely classloader to have access to our Enums
    val mirror = ru.runtimeMirror(classLoader)

    Option(clazz.companion)
      .filter(_.isModule)
      .map(s => mirror.reflectModule(s.asModule).instance)
      .filter(targetEnumClass.isInstance(_))
      .map(_.asInstanceOf[enumeratum.Enum[E]])
      .map(_.values.toList)
      .getOrElse(Nil)
  }
}
