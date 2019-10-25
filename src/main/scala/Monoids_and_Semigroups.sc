/**
 * A monoid for a type A is:
 *  - an operation `combine` with type `(A, A) => A`
 *  - an element `empty` of type `A`
 *
 * In addition to providing the `combine` and `empty` operations, monoid must formally
 * obey several laws.
 *
 * `combine` must be associative
 * `empty` must be an identity element
 */
trait Monoid[A] {
  def combine(x: A, y: A): A

  def empty: A

  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]) = {
    m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
  }

  def identityLaw[A](x: A)(implicit m: Monoid[A]) = {
    (m.combine(x, m.empty)) == x && (m.combine(m.empty, x) == x)
  }
}


/**
 * A semigroup is just the `combine` part of a monoid.
 */
trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}
