
case class State[S, +A](run: S => (A, S)) {
  def map[B](f: A => B): State[S, B] = flatMap(a => State(s => (f(a), s)))
  def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
    val (a, next) = run(s)
    f(a).run(next)
  })
}

object State {
  def get[S]: State[S, S] = State(s => (s, s))
  def set[S](s: S): State[S, Unit] = State(_ => ((), s))
  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get
    _ <- set(f(s))
  } yield ()
}

import State._

def succ(n: Int): Int = modify[Int](_ + 1).run(n)._2

println(succ(2))

def succAndTwice(n: Int): (Int, Int) = (for {
  a <- get[Int]
  _ <- set(a + 1)
  _ <- modify[Int](_ * 2)
} yield a).run(n)

println(succAndTwice(5))

def succAndTwice2(n: Int): (Int, Int) = get[Int].flatMap { a =>
  set(a + 1).flatMap { _ =>
    modify[Int](_ * 2).map { _ =>
      a
    }
  }
}.run(n)

println(succAndTwice2(5))
