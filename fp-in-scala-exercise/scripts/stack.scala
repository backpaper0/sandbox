case class State[S, +A](run: S => (A, S)) {
  def map[B](f: A => B): State[S, B] = flatMap(a => State(s => (f(a), s)))
  def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
    val (a, next) = run(s)
    f(a).run(next)
  })
}

case class Stack[A](as: List[A]) {
  def pop: (A, Stack[A]) = (as.head, Stack(as.tail))
  def push(a: A): (Unit, Stack[A]) = ((), Stack(a :: as))
}

object Stack {
  def empty[A]: Stack[A] = Stack(Nil)
  def pop[A]: State[Stack[A], A] = State(s => s.pop)
  def push[A](a: A): State[Stack[A], Unit] = State(s => s.push(a))
}

val stack1 = Stack.empty[Int]
val (_, stack2) = stack1.push(1)
val (_, stack3) = stack2.push(2)
val (_, stack4) = stack3.push(3)
val (a1, stack5) = stack4.pop
val (a2, stack6) = stack5.pop
val (a3, stack7) = stack6.pop
val a = a1 * a2 + a3

assert(a == 7)

import Stack._

val (b, _) = (for {
  _ <- push(1)
  _ <- push(2)
  _ <- push(3)
  b1 <- pop
  b2 <- pop
  b3 <- pop
} yield b1 * b2 + b3).run(stack1)

assert(b == 7)

