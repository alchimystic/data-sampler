# data-sampler

Utility library to generate data, based on Type classes and using Shapeless for dealing with Products

### Sampler[T]: a generator of instances of T

This small framework is based on Type classes, enabling ad-hoc polymorphism.

A type class is basically a way to add functionality to a class without the need of changing it.

**Sampler[T]** is our core type class, and it represents a generator of instances of type T.

While you can implement your own Sampler[T], the goal is to be able to derive as many Samplers as possible without writing any code.

In Scala, type classes rely on implicits for doing the heavy-duty work of looking up the correct implementations.

So with 3 or 4 imports you should be able to generate instances of most case classes in Scala.

### Algebraic Data Types

ADTs play an essential role in Functional Programming, including Scala.
The most important data types are **Sum** and **Product**.
A Sum data type represents an **OR** relation, so a sum T can be one of many other types. This is simple polymorphism and in Scala is done using sealed traits.
A Product data type represents an **AND** relation, so a product T has other types in it. This is a case class in Scala.  

So in Scala we can say that any domain model will contain a finite set of data types representing a deep and wide combination of Sums and Products.

Regarding Products, the framework is able to derive Sampler for a case class T(A, B), if it has Samplers for A and B. 
This is done using **Shapeless**, which gives us a way of reasoning about Products in a type-safe recursive way (and avoid reflection, for example).

Regarding Sums, the framework is not able to automatically derive a Sampler as it doesn't know which implementation of a sealed trait to pick.
We can however hint on the use of a certain implementation on a certain context.


### Scalar data types

ADTs allows us to define a rich and deep set of data types, but in the very end this relies on a small set of well know scalar types, holding a single value.
Int, Double, Boolean, Strings are some of examples of these scalar types, most of them are primitives. 
Other types (like BigDecimal, Instant or LocalDateTime) are a bit more complex, but they still represent a single value.
At the code, the framework must provide Samplers for these types.

The samplers for all these core scalar types are available in trait **CoreSamplerSet**.
We have 2 object implementations of CoreSamplerSet: 
* **FixedSamplers**: samplers providing always the same value for its type
* **UniqueSamplers**: samplers providing unique (as much as possible) values for its type 

### Collection data types

Some built-in types are not scalar, but are still very important and widely used: Seq, List, Set, Option, Map.

These represent a container for other types, and in a broad sense we can call them _Collections_.

We have 2 objects with Samplers for collections:
* **EmptySamplers**: samplers providing always the empty case of the collection (Nil, None, etc..)
* **NonEmptySamplers**: samplers providing always a collection with one element

### Enumerated / enumerations

Enumerations are a special case of a scalar type. To bridge enumerations with Samplers, we have the type class **Enumerated[T]**
The Enumerated[T] represents a type class which knows all the possible values of T.

It's more or less consensual than enumeratum provides a much better and flexible implementation of enumerations than Scala built-in enums
For that reason, for now we can only derive Enumerated for any T extending enumeratum's EnumEntry.
Later we expect to add support for Scala enums.

If an Enumerated[T] can be provided, a Sampler[T] can be derived.
Any of the implementations of **CoreSamplerSet** can provide this.

### Deriving the appropriate Samplers

Deriving automatically the Samplers for Scala ADTs is just a matter of picking the right imports:
* always import Samplers._, needed for the basic Sampler machinery and deriving samplers for Products (case classes)
* import FixedSamplers or UniqueSamplers, depending on the needs of each case
* import EmptySamplers or NonEmptySamplers, depending on how simplistic/empty or thorough/deep we need data in each case


### How to use

The most natural use case is to generate test data.
Its a pain to write tests when we have case classes with many non-optional fields, especially when each test only cares with a limited set of those fields.
The usual approach is to define a set of constant values, and then use them to create more complex types.
In the end of the test we would compare the results with constants we defined.

I always used a different approach: I create the complex domain instances i need, and then the 'constants' are retrieved from it.

Some hints on how to get the best out of this framework:
* If its important to have unique identifiers (Int, Long, String) import UniqueSamplers, otherwise FixedSamplers will to the job
* If our test need to be thorough use deeply filled datatypes (eg: testing a Codec), we should import NonEmptySamplers, otherwise EmptySamplers will do
* Customize sampled data by mutating it with copy(). I usually mutate only the fields which are relevant to each test case
* Use Samplers.select() to pick a sampler for a generic type (sealed trait / sum datatype). Eg:  Samplers.select[Animal,Bird] will provide a bird whenever an animal is needed 






