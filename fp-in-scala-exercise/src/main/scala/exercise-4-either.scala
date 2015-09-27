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
