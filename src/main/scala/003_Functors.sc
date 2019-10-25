/**
 * Functors, an abstraction that allows us to represent sequences of operations within
 * a context such as a `List`, an `Option`, and so on.
 *
 * Informally, a functor is anything with a `map` method.
 * Like `Option`, `List`, `Either`.
 *
 */

// The map methods of List, Option, and Either apply functions eagerly.


// Futures
// `Future` is a functor that sequences asynchronous computations by queueing them and
// applying them as their predecessors complete.

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

val future: Future[String] =
  Future(123)
    .map(_ + 1)
    .map(_ * 2)
    .map(_ + "!")

Await.result(future, 1.second)

/**
 * Note that Scala’s Futures aren’t a great example of pure functional programming
 * because they aren’t referentially transparent. Future always computes and caches
 * a result and there’s no way for us to tweak this behaviour. This means we can get
 * unpredictable results when we use Future to wrap side-effecting computations.
 */

import cats.instances.function._
import cats.syntax.functor._

val func =
  ((x: Int) => x.toDouble)
    .map(_ + 1)
    .map(_ * 2)
    .map(_ + "!")

func(123)


/**
 * A functor is a type `F[A]` with an operation `map` with type (A => B) => F[B].
 */
/*
package cats

import scala.language.higherKinds

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
*/


import scala.language.higherKinds
import cats.Functor
import cats.instances.list._
import cats.instances.option._

val list1 = List(1,2,3)
val list2 = Functor[List].map(list1)(_ * 2)

val option1 = Option(33)
val option2 = Functor[Option].map(option1)(_.toString)














