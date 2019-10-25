/**
 * Cats contains a wide variety of functional programming tools
 * The majority of these tools are delivered in the form of type classes that
 * we can apply to existing Scala types.
 *
 * Type classes are a programming pattern originating in Haskell
 * They allow us to extend existing libraries with new functionality, without
 * using traditional inheritance, and without altering the original library
 * source code.
 *
 *
 * There are three important components to the type class pattern: the type
 * class itself, instances for particular types, and the interface methods
 * that we expose to users.
 */
sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
case object JsNull extends Json

// Type class
trait JsonWriter[A] {
  def write(value: A): Json
}

// Type class instances
// In Scala we define instances by creating concrete implementations of the
// type class and tagging them with the implicit keyword:
final case class Person(name: String, email: String)

object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] =
    new JsonWriter[String] {
      override def write(value: String) = JsString(value)
    }

  implicit val personWriter: JsonWriter[Person] =
    new JsonWriter[Person] {
      override def write(value: Person) =
        JsObject(Map(
          "name" -> JsString(value.name),
          "email" -> JsString(value.email)
        ))
    }

  // ...
}

// Type class interfaces
// A type class interface is any functionality we expose to users.
// Interfaces are generic methods that accept instances of the type class as
// ``implicit`` parameters.
// There are two common ways of specifying an interface: ``Interface Objects``
// and ``Interface Syntax``.

// Interface Objects
object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
    w.write(value)
}

// to use this object, we import any type class instances we care about and
// call the relevant method:
import JsonWriterInstances._

Json.toJson(Person("Dave", "dave@example.com"))


// Interface syntax
object JsonSyntax {
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }
}

// We use interface syntax by importing it alongside the instances for the
// types we need:
import JsonWriterInstances._
import JsonSyntax._

Person("Dave", "dave@example.com").toJson

implicitly[JsonWriter[Person]]



/**
 * Working with type classes in Scala means working with implicit values and
 * implicit parameters.
 *
 * There are a few rules we need to know to do this effectively.
 *
 * - Packaging Implicits
 * ```any definitions marked `implicit` in Scala must be placed inside an object or
 * trait rather than at the top level.```
 * 
 * 
 */
