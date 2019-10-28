/**
 * Informally, a monad is anything with a constructor and a `flatMap` method.
 * We even have special syntax to support monads: for comprehensions.
 *
 * A monad is a mechanism for sequencing computations.
 *
 * Every monad is also a functor
 */
def parseInt(str: String): Option[Int] =
  scala.util.Try(str.toInt).toOption

def divide(a: Int, b: Int): Option[Int] =
  scala.util.Try(a / b).toOption

def stringDivideBy(aStr: String, bStr: String): Option[Int] =
  parseInt(aStr).flatMap { aNum =>
    parseInt(bStr).flatMap { bNum =>
      divide(aNum, bNum)
    }
  }

def stringDivideBy2(aStr: String, bStr: String): Option[Int] =
  for {
    aNum <- parseInt(aStr)
    bNum <- parseInt(bStr)
    ans <- divide(aNum, bNum)
  } yield ans


stringDivideBy("6", "2")
stringDivideBy("6", "0")
stringDivideBy("6", "foo")
stringDivideBy("bar", "2")




// Lists
for {
  x <- (1 to 3).toList
  y <- (4 to 5).toList
} yield (x, y)


// Futures
// `Future` is a monad that sequences computations without worrying that they are
// asynchronous.

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

def doSomethingLongRunning: Future[Int] = ???
def doSomethingElseLongRunning: Future[Int] = ???

def doSomething: Future[Int] =
  for {
    res1 <- doSomethingLongRunning
    res2 <- doSomethingElseLongRunning
  } yield res1 + res2


/**
 * Definition of a Monad
 *
 * Monadic behaviour is formally captured in two operations:
 * `pure`, of type `A => F[A]`
 * `flatMap`, of type `(F[A], A => F[B]) => F[B]`
 *
 * `pure` abstracts over constructors, providing a way to create a new monadic context
 * from a plain value.
 * `flatMap` provides the sequencing step we have already discussed, extracting the value
 * from a context and generating the next context in the sequence.
 */

import scala.language.higherKinds

trait Monad[F[_]] {
  def pure[A](value: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
}

/**
 * Monad Laws
 */
trait MonadLaws {
  /**
   * Left identity: calling `pure` and transforming the result with `func` is the same as
   * calling `func`:
   *
   * pure(a).flatMap(func) == func(a)
   */

  /**
   * Right identity: passing `pure` to `flatMap` is the same as doing nothing
   *
   * m.flatMap(pure) == m
   */

  /**
   * Associativity: `flatMapping` over two functions `f` and `g` is the same as `flatMapping`
   * over `f` and then `flatMapping` over `g`:
   *
   * m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
   */
}


import cats.Monad
import cats.instances.option._
import cats.instances.list._

val opt1 = Monad[Option].pure(3)
val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
val opt3 = Monad[Option].map(opt2)(a => a * 100)

val list1 = Monad[List].pure(3)
val list2 = Monad[List].flatMap(list1)(a => List(a, a * 10))
val list3 = Monad[List].map(list2)(a => a + 123)


/**
 * Cats provides instances for all the monads in the standard library (Option, List, Vector
 * and so on) via `cats.instances`(https://github.com/typelevel/cats/blob/master/core/src/main/scala-2.12/cats/instances/package.scala):
 */
// Option

import cats.instances.option._

Monad[Option].flatMap(Some(1))(x => Option(x * 2))

// List

import cats.instances.list._

Monad[List].flatMap(List(1, 2, 3))(x => List(x, x * 10))

// Vector

import cats.instances.vector._

Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a * 10))

// Future

import cats.instances.future._
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

val fm = Monad[Future]
val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 1))


// Monad Syntax
//
// The syntax for monads comes from three places:
// `cats.syntax.flatMap` provides syntax for `flatMap1
// `cats.syntax.functor` provides syntax for `map`
// `cats.syntax.applicative` provides syntax for `pure`

import cats.instances.option._
import cats.instances.list._
import cats.syntax.applicative._

1.pure[Option] // res: Option[Int] = Some(1)
1.pure[List] // res: Option[List] = List(1)



// Either
// In Scala 2.12, `Either` was redesigned.
// The modern `Either` makes the decision that the right side represents the success
// case and thus supports `map` and `flatMap` directly.

val either1: Either[String, Int] = Right(10)
val either2: Either[String, Int] = Right(32)

for {
  a <- either1.right
  b <- either2.right
} yield a + b

import cats.syntax.either._

val a = 3.asRight[String]     // a: Either[String, Int] = Right(3)
val b = 4.asRight[String]     // b: Either[String, Int] = Right(4)

for {
  x <- a
  y <- b
} yield x * x + y * y















