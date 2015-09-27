package exercise4

sealed trait Option[+A] {

  //EXERCISE 4.1 (P.67)

  def map[B](f: A => B): Option[B] = this match {
    case None => None
    case Some(value) => Some(f(value))
  }

  def flatMap[B](f: A => Option[B]): Option[B] = map(f).getOrElse(None)

  def getOrElse[B >: A](default: => B): B = this match {
    case None => default
    case Some(value) => value
  }

  def orElse[B >: A](ob: => Option[B]): Option[B] = map(Some(_)).getOrElse(ob)

  def filter(f: A => Boolean): Option[A] = flatMap(a => if(f(a)) Some(a) else None)
}

case class Some[+A](get: A) extends Option[A]

case object None extends Option[Nothing]

object Option {

  //EXERCISE 4.2 (P.68)
  def variance(xs: Seq[Double]): Option[Double] = {
    val om = if(xs.isEmpty) None else Some(xs.sum / xs.size)
    om.flatMap { m =>
      val ys = xs.map(x => math.pow(x - m, 2))
      if(ys.isEmpty) None else Some(ys.sum / ys.size)
    }
  }

  //EXERCISE 4.3 (P.72)
  def map2[A, B, C](oa: Option[A], ob: Option[B])(f: (A, B) => C): Option[C] = (oa, ob) match {
    case (Some(a), Some(b)) => Some(f(a, b))
    case _ => None
  } 

  //EXERCISE 4.4 (P.72)
  def sequence[A](as: List[Option[A]]): Option[List[A]] = {
    @annotation.tailrec
    def f(as2: List[Option[A]], as3: List[A]): Option[List[A]] = as2 match {
      case Some(h) :: Nil => Some((h :: as3).reverse)
      case Some(h) :: t => f(t, h :: as3)
      case None :: _ => None
      case Nil => Some(Nil)
    }
    f(as, Nil:List[A])
  }

  //EXERCISE 4.5 (P.73)
  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = {
    @annotation.tailrec
    def g(as2: List[A], bs: List[B]): Option[List[B]] = as2 match {
      case h :: Nil => f(h) match {
        case Some(b) => Some((b :: bs).reverse)
        case None => None
      }
        case h :: t => f(h) match {
          case Some(b) => g(t, b :: bs)
          case None => None
        }
          case _ => Some(Nil)
    }
    g(as, Nil:List[B])
  }
}
