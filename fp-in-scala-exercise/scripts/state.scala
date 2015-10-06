import scala.language.higherKinds

case class State[S, +A](run: S => (A, S)) {
  def map[B](f: A => B): State[S, B] = flatMap(a => State(s => (f(a), s)))
  def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
    val (a, next) = run(s)
    f(a).run(next)
  })
}

object State {
  def unit[S, A](a: A): State[S, A] = State(s => (a, s))
  def get[S]: State[S, S] = State(s => (s, s))
  def set[S](s: S): State[S, Unit] = State(_ => ((), s))
  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get
    _ <- set(f(s))
  } yield ()
}

import State._

def succ(n: Int): Int = modify[Int](_ + 1).run(n)._2

assert(succ(2) == 3)

def succAndTwice(n: Int): (Int, Int) = (for {
  a <- get[Int]
  _ <- set(a + 1)
  _ <- modify[Int](_ * 2)
} yield a).run(n)

assert(succAndTwice(5) == (5, 12))

def succAndTwice2(n: Int): (Int, Int) = get[Int].flatMap { a =>
  set(a + 1).flatMap { _ =>
    modify[Int](_ * 2).map { _ =>
      a
    }
  }
}.run(n)

assert(succAndTwice2(5) == (5, 12))


// Stack

type Stack[A] = List[A]

object Stack {
  def empty[A]: Stack[A] = Nil
  def pop[A]: State[Stack[A], A] = State(s => (s.head, s.tail))
  def push[A](a: A): State[Stack[A], Unit] = State(s => ((), a :: s))
}

import Stack._

val stack1 = Stack.empty[Int]
val (_, stack2) = push(1).run(stack1)
val (_, stack3) = push(2).run(stack2)
val (_, stack4) = push(3).run(stack3)
val (a1, stack5) = pop.run(stack4)
val (a2, stack6) = pop.run(stack5)
val (a3, stack7) = pop.run(stack6)
val a = a1 * a2 + a3

assert(a == 7)

val (b, _) = (for {
  _ <- push(1)
  _ <- push(2)
  _ <- push(3)
  b1 <- pop
  b2 <- pop
  b3 <- pop
} yield b1 * b2 + b3).run(stack1)

assert(b == 7)

// Monad

trait Monad[F[_]] {
  def unit[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(a => unit(f(a)))
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = flatMap(fa)(a => map(fb)(b => f(a, b)))
}

def stateMonad[S]: Monad[({ type F[A] = State[S, A] })#F] = new Monad[({ type F[A] = State[S, A] })#F] {
  def unit[A](a: A): State[S, A] = State.unit(a)
  def flatMap[A, B](fa: State[S, A])(f: A => State[S, B]): State[S, B] = fa flatMap f
}
