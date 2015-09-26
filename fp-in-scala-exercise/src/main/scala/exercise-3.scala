package exercise3

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
//  def sum(ints: List[Int]): Int = ints match {
//    case Nil => 0
//    case Cons(x, xs) => x + sum(xs)
//  }
  def apply[A](as: A*): List[A] = if(as.isEmpty) Nil else Cons(as.head, apply(as.tail: _*))

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = as match {
    case Nil => z
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

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
  def dropWhile[A](l: List[A])(f: A => Boolean): List[A] = l match {
    case Cons(h, t) => if(f(h)) dropWhile(t)(f) else l
    case Nil => Nil
  }

  //EXERCISE 3.6 (P.46)
  def init[A](l: List[A]): List[A] = l match {
    case Cons(h, Nil) => Nil
    case Cons(h, t) => Cons(h, init(t))
    case Nil => Nil
  }

  //EXERCISE 3.10 (P.51)
  def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B = {
    @annotation.tailrec
    def g(as2: List[A], b: B): B = as2 match {
      case Cons(h, t) => g(t, f(b, h))
      case Nil => b
    }
    g(as, z)
  }

  //EXERCISE 3.11 (P.51)
  def sum(ints: List[Int]): Int = foldLeft(ints, 0)(_ + _)
  def product(ds: List[Double]): Double = foldLeft(ds, 1.0)(_ * _)
  def length[A](xs: List[A]): Int = foldLeft(xs, 0)((b, a) => b + 1)

  //EXERCISE 3.12 (P.51)
  def reverse[A](as: List[A]): List[A] = foldLeft(as, (Nil:List[A]))((b, a) => Cons(a, b))

  //EXERCISE 3.13 (P.51)
  //あとで考える
  def foldLeft2[A, B](as: List[A], z: B)(f: (B, A) => B): B = ???

  //EXERCISE 3.14 (P.51)
  def append[A](a1: List[A], a2: List[A]): List[A] = foldRight(a1, a2)(Cons(_, _))

  //EXERCISE 3.15 (P.51)
  //あとで考える
  def append2[A](as: List[A]*): List[A] = ???

  //EXERCISE 3.16 (P.52)
  def increment(as: List[Int]): List[Int] = map(as)(_ + 1)

  //EXERCISE 3.17 (P.52)
  def doubleToString(as: List[Double]): List[String] = map(as)(_.toString)

  //EXERCISE 3.18 (P.52)
  def map[A, B](as: List[A])(f: A => B): List[B] = as match {
    case Nil => Nil
    case Cons(h, t) => Cons(f(h), map(t)(f))
  }
}
