package exercise5

trait Stream[+A] {
  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, t) => Some(h())
  }
  def toList: List[A] = {
    @annotation.tailrec
    def f(s: Stream[A], l: List[A]): List[A] = s match {
      case Empty => l.reverse
      case Cons(h, t) => f(t(), h() :: l)
    }
    f(this, Nil)
  }
  def take(n: Int): Stream[A] = if (n == 0) Stream.empty else this match {
    case Empty => Stream.empty
    case Cons(h, t) => Stream.cons(h(), t().take(n - 1))
  }
  def drop(n: Int): Stream[A] = if (n == 0) this else this match {
    case Empty => Stream.empty
    case Cons(h, t) => t().drop(n - 1)
  }
  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    case Cons(h, t) => f(h(), t().foldRight(z)(f))
    case _ => z
  }
  def forAll(p: A => Boolean): Boolean = foldRight(true)((a, b) => p(a) && b)
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }
  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))
}
