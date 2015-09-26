package exercise3

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }
  def apply[A](as: A*): List[A] = if(as.isEmpty) Nil else Cons(as.head, apply(as.tail: _*))

  //EXERCISE 3.2 (P.44)
  def tail[A](as: List[A]): List[A] = as match {
    case Cons(h, t) => t
    case Nil => Nil
  }

  //EXERCISE 3.3 (P.45)
  def setHead[A](as: List[A], a: A): List[A] = as match {
    case Cons(h, t) => Cons(a, t)
    case Nil => Nil
  }

  //EXERCISE 3.4 (P.45)
  def drop[A](l: List[A], n: Int): List[A] = if(n < 1) l else l match {
    case Cons(h, t) => drop(t, n - 1)
    case Nil => Nil
  }

  //EXERCISE 3.5 (P.45)
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Cons(h, t) => if(f(h)) dropWhile(t, f) else l
    case Nil => Nil
  }

  //EXERCISE 3.6 (P.46)
  def init[A](l: List[A]): List[A] = l match {
    case Cons(h, Nil) => Nil
    case Cons(h, t) => Cons(h, init(t))
    case Nil => Nil
  }
}
