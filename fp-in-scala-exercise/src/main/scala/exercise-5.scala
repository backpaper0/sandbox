package exercise5

trait Stream[+A] {
  import Stream._
  def headOption: Option[A] = foldRight(None:Option[A])((a, b) => Some(a))
  def toList: List[A] = {
    @annotation.tailrec
    def f(s: Stream[A], l: List[A]): List[A] = s match {
      case Empty => l.reverse
      case Cons(h, t) => f(t(), h() :: l)
    }
    f(this, Nil)
  }
  def take(n: Int): Stream[A] = if (n == 0) empty else this match {
    case Empty => empty
    case Cons(h, t) => cons(h(), t().take(n - 1))
  }
  def drop(n: Int): Stream[A] = if (n == 0) this else this match {
    case Empty => empty
    case Cons(h, t) => t().drop(n - 1)
  }
  def dropWhile(p: A => Boolean): Stream[A] = this match {
    case Empty => empty
    case Cons(h, t) => if (p(h())) t().dropWhile(p) else this
  }
  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    case Cons(h, t) => f(h(), t().foldRight(z)(f))
    case _ => z
  }
  def forAll(p: A => Boolean): Boolean = foldRight(true)((a, b) => p(a) && b)
  def takeWhile(p: A => Boolean): Stream[A] = foldRight(empty[A])((a, b) => if (p(a)) cons(a, b) else empty)
  def map[B](f: A => B): Stream[B] = foldRight(empty[B])((a, b) => cons(f(a), b))
  def filter(p: A => Boolean): Stream[A] = foldRight(empty[A])((a, b) => if(p(a)) cons(a, b) else b)
  def append[B >: A](s: Stream[B]): Stream[B] = foldRight(s)((a, b) => cons(a, b))
  def flatMap[B](f: A => Stream[B]): Stream[B] = foldRight(empty[B])((a, b) => f(a).append(b))
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

  def apply[A](as: A*): Stream[A] = {
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))
  }

  def constant[A](a: A): Stream[A] = {
    lazy val s: Stream[A] = cons(a, s)
    s
  }
}
