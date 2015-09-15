package study

//import scala.language.higherKinds

trait Functor[A, F[_]] { self: F[A] =>
  def map[B](f: A => B): F[B]
}

trait Apply[A, F[_]] extends Functor[A, F] { self: F[A] =>
  def ap[B](f: F[A => B]): F[B]
}

sealed trait Maybe[A] extends Apply[A, Maybe]

case class Just[A](value: A) extends Maybe[A] {
  def map[B](f: A => B): Maybe[B] = Just(f(value))
  def ap[B](f: Maybe[A => B]): Maybe[B] = f match {
    case Just(g) => Just(g(value))
    case Nothing() => Nothing()
  }
}

case class Nothing[A]() extends Maybe[A] {
  def map[B](f: A => B): Maybe[B] = Nothing()
  def ap[B](f: Maybe[A => B]): Maybe[B] = Nothing()
}

sealed trait List[A] extends Apply[A, List]

case class Cons[A](head: A, tail: List[A]) extends List[A] {
  def map[B](f: A => B): List[B] = Cons(f(head), tail.map(f))
  def ap[B](f: List[A => B]): List[B] = {
    def inner(x: A, xs: List[A], fs: List[A => B]): List[B] = (xs, fs) match {
      case (Nil(), Cons(h2, Nil())) => Cons(h2(x), Nil())
      case (Nil(), Cons(h2, t2)) => Cons(h2(x), inner(head, tail, t2))
      case (Cons(h1, t1), Cons(h2, _)) => Cons(h2(x), inner(h1, t1, fs))
      case _ => Nil()
    }
    inner(head, tail, f)
  }
}

case class Nil[A]() extends List[A] {
  def map[B](f: A => B): List[B] = Nil()
  def ap[B](f: List[A => B]): List[B] = Nil()
}
