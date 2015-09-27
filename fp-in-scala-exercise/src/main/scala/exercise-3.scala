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

  //EXERCISE 3.19 (P.52)
  //EXERCISE 3.21 (P.53)
  def filter[A, B](as: List[A])(f: A => Boolean): List[A] = flatMap(as)(a => if(f(a)) List(a) else Nil)

  //EXERCISE 3.20 (P.53)
  def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = {
    def map2(bs1: List[B], bs2: List[B]): List[B] = bs1 match {
      case Nil => bs2
      case Cons(h, t) => Cons(h, map2(t, bs2))
    }
    def g(as2: List[A]): List[B] = as2 match {
      case Nil => Nil
      case Cons(h, t) => map2(f(h), g(t))
    }
    g(as)
  }

  //EXERCISE 3.22 (P.53)
  def sum(as1: List[Int], as2: List[Int]): List[Int] = zipWith(as1, as2)(_ + _)

  //EXERCISE 3.23 (P.53)
  def zipWith[A](as1: List[A], as2: List[A])(f: (A, A) => A): List[A] = (as1, as2) match {
    case (Cons(h1, t1), Cons(h2, t2)) => Cons(f(h1, h2), zipWith(t1, t2)(f))
    case _ => Nil
  }

  //EXERCISE 3.24 (P.54)
  //あとで考える
  def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = ???
}

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {

  //EXERCISE 3.25 (P.56)
  def size[A](t: Tree[A]): Int = t match {
    case Leaf(_) => 1
    case Branch(l, r) => 1 + size(l) + size(r)
  }
}

