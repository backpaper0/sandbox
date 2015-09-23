package exercise_10

trait Monoid[A] {
  def op(a1: A, a2: A): A
  def zero: A
}

package object monoid {
  val intAddition: Monoid[Int] = new Monoid[Int] {
    def op(a1: Int, a2: Int): Int = a1 + a2
    def zero: Int = 0
  }
  val intMultiplication: Monoid[Int] = new Monoid[Int] {
    def op(a1: Int, a2: Int): Int = a1 * a2
    def zero: Int = 1
  }
  val booleanOr: Monoid[Boolean] = new Monoid[Boolean] {
    def op(a1: Boolean, a2: Boolean): Boolean = a1 || a2
    def zero: Boolean = false
  }
  val booleanAnd: Monoid[Boolean] = new Monoid[Boolean] {
    def op(a1: Boolean, a2: Boolean): Boolean = a1 && a2
    def zero: Boolean = true
  }
  
  def optionMonoid[A](m: Monoid[A]): Monoid[Option[A]] = new Monoid[Option[A]] {
    def op(a1: Option[A], a2: Option[A]): Option[A] = (a1, a2) match {
      case (Some(x1), Some(x2)) => Some(m.op(x1, x2))
      case (Some(_), None) => a1
      case (None, Some(_)) => a2
      case (None, None) => None
    }
    def zero: Option[A] = None
  }
}
