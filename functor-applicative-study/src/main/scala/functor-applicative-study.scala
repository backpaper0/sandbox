package study

//import scala.language.higherKinds

trait Functor[F[_]] {
  def map[A, B](a: F[A], f: A => B): F[B]
}

trait Apply[F[_]] extends Functor[F] {
  def ap[A, B](a: F[A], f: F[A => B]): F[B]
}

trait Applicative[F[_]] extends Apply[F] {
  def point[A](a: A): F[A]
}

package object implicits {
  implicit class ApplyWrapper[A, F[_]](val value: F[A]) {
    def map[B](f: A => B)(implicit functor: Apply[F]): F[B] = functor.map(value, f)
    def ap[B](f: F[A => B])(implicit functor: Apply[F]): F[B] = functor.ap(value, f)
  }
  implicit class ApplicativeWrapper[A](val value: A) {
    def point[F[_]](implicit applicative: Applicative[F]): F[A] = applicative.point(value)
  }
  implicit object MaybeApplicative extends Applicative[Maybe] {
    def map[A, B](a: Maybe[A], f: A => B): Maybe[B] = a match {
      case Just(value) => Just(f(value))
      case Nothing => Nothing.asInstanceOf[Maybe[B]]
    }
    def ap[A, B](a: Maybe[A], f: Maybe[A => B]): Maybe[B] = (a, f) match {
      case (Just(value), Just(fn)) => Just(fn(value))
      case _ => Nothing.asInstanceOf[Maybe[B]]
    }
    def point[A](a: A): Maybe[A] = Just(a)
  }
  implicit object ListApplicative extends Applicative[List] {
    def map[A, B](a: List[A], f: A => B): List[B] = a match {
      case Cons(head, tail) => Cons(f(head), map(tail, f))
      case Nil => Nil.asInstanceOf[List[B]]
    }
    def ap[A, B](a: List[A], f: List[A => B]): List[B] = {
      def ap2(a2: List[A], f2: List[A => B]): List[B] = (a2, f2) match {
        case (Cons(head, tail), Cons(fn, ftail)) => Cons(fn(head), ap2(tail, f2))
        case (Nil, Cons(fn, ftail)) =>  ap2(a, ftail)
        case _ => Nil.asInstanceOf[List[B]]
      }
      ap2(a, f)
    }
    def point[A](a: A): List[A] = Cons(a, Nil.asInstanceOf[List[A]])
  }
  def functionFunctor[ARG]: Functor[({ type F[A] = ARG => A })#F] = new Functor[({ type F[A] = ARG => A })#F] {
    def map[A, B](a: ARG => A, f: A => B): ARG => B = a andThen f
  }
}

sealed trait Maybe[A]

case class Just[A](value: A) extends Maybe[A]

case object Nothing extends Maybe[scala.Nothing]

sealed trait List[A]

case class Cons[A](head: A, tail: List[A]) extends List[A]

case object Nil extends List[scala.Nothing]

