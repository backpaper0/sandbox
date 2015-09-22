package study

//import scala.language.higherKinds

trait Functor[F[_]] {
  def map[A, B](a: F[A], f: A => B): F[B]
}

object Functors {
  implicit class FunctorWrapper[A, F[_]](val value: F[A]) {
    def map[B](f: A => B)(implicit functor: Functor[F]): F[B] = functor.map(value, f)
  }
  implicit object MaybeFunctor extends Functor[Maybe] {
    def map[A, B](a: Maybe[A], f: A => B): Maybe[B] = a match {
      case Just(value) => Just(f(value))
      case Nothing => Nothing.asInstanceOf[Maybe[B]]
    }
  }
  implicit object ListFunctor extends Functor[List] {
    def map[A, B](a: List[A], f: A => B): List[B] = a match {
      case Cons(head, tail) => Cons(f(head), map(tail, f))
      case Nil => Nil.asInstanceOf[List[B]]
    }
  }
}

sealed trait Maybe[A]

case class Just[A](value: A) extends Maybe[A]

case object Nothing extends Maybe[scala.Nothing]

sealed trait List[A]

case class Cons[A](head: A, tail: List[A]) extends List[A]

case object Nil extends List[scala.Nothing]

