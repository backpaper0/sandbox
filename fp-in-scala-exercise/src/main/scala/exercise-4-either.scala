package exercise4

sealed trait Either[+E, +A] {

  //EXERCISE 4.6 (P.76)

  def map[B](f: A => B): Either[E, B] = flatMap(a => Right(f(a)))

  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
    case Left(value) => Left(value)
    case Right(value) => f(value)
  }

  def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = this match {
    case Left(_) => b
    case Right(_) => this
  }

  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = (this, b) match {
    case (Right(a), Right(b)) => Right(f(a, b))
    case (Left(value), _) => Left(value)
    case (_, Left(value)) => Left(value)
  }
}

case class Left[+E](value: E) extends Either[E, Nothing]

case class Right[+A](value: A) extends Either[Nothing, A]

object Either {

  //EXERCISE 4.7
  def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = {
    @annotation.tailrec
    def f(es2: List[Either[E, A]], as: List[A]): Either[E, List[A]] = es2 match {
      case Right(h) :: Nil => Right((h :: as).reverse)
      case Right(h) :: t => f(t, h :: as)
      case Left(value) :: _ => Left(value)
      case Nil => Right(Nil)
    }
    f(es, Nil)
  }

  def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = {
    @annotation.tailrec
    def g(as2: List[A], bs: List[B]): Either[E, List[B]] = as2 match {
      case h :: Nil => f(h) match {
        case Right(value) => Right((value :: bs).reverse)
        case Left(value) => Left(value)
      }
      case h :: t => f(h) match {
        case Right(value) => g(t, value :: bs)
        case Left(value) => Left(value)
      }
        case _ => Right(Nil)
    }
    g(as, Nil)
  }
}
