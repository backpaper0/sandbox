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
  def take(n: Int): Stream[A] = unfold((this, 0)) {
    case (Cons(h, t), m) if m < n => Some((h(), (t(), m + 1)))
    case _ => None
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
  def takeWhile(p: A => Boolean): Stream[A] = unfold(this) {
    case Cons(h, t) if p(h()) => Some((h(), t()))
    case _ => None
  }
  def map[B](f: A => B): Stream[B] = unfold(this) {
    case Empty => None
    case Cons(h, t) => Some((f(h()), t()))
  }
  def filter(p: A => Boolean): Stream[A] = foldRight(empty[A])((a, b) => if(p(a)) cons(a, b) else b)
  def append[B >: A](s: Stream[B]): Stream[B] = foldRight(s)((a, b) => cons(a, b))
  def flatMap[B](f: A => Stream[B]): Stream[B] = foldRight(empty[B])((a, b) => f(a).append(b))
  def zipWith[B >: A](s: Stream[B])(f: (B, B) => B): Stream[B] = unfold((this, s)) {
    case (Cons(h1, t1), Cons(h2, t2)) => Some((f(h1(), h2()), (t1(), t2())))
    case _ => None
  }
  def zipAll[B](s: Stream[B]): Stream[(Option[A], Option[B])] = unfold((this, s)) {
    case (Cons(h1, t1), Cons(h2, t2)) => Some(((Some(h1()), Some(h2())), (t1(), t2())))
    case (Cons(h1, t1), Empty) => Some(((Some(h1()), None), (t1(), empty)))
    case (Empty, Cons(h2, t2)) => Some(((None, Some(h2())), (empty, t2())))
    case _ => None
  }
  def startsWith[B >: A](s: Stream[B]): Boolean = zipAll(s).foldRight(true)((a, b) => a match {
    case (Some(x), Some(y)) if x == y => b
    case (Some(_), None) => true
    case _ => false
  })
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

  def ones: Stream[Int] = unfold(1)(s => Some((s, s)))
  def constant[A](a: A): Stream[A] = unfold(a)(s => Some((s, s)))
  def from(n: Int): Stream[Int] = unfold(n)(s => Some((s, s + 1)))
  def fibs: Stream[Int] = unfold((0, 1)){ case (m, n) => Some((m, (n, m + n))) }
  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = {
    def g(s: S): Stream[A] = f(s) match {
      case Some((a, t)) => cons(a, g(t))
      case None => empty
    }
    g(z)
  }
}
