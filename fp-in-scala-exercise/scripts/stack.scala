case class Stack[A](as: List[A]) {
  def pop: (A, Stack[A]) = (as.head, Stack(as.tail))
  def push(a: A): (Unit, Stack[A]) = ((), Stack(a :: as))
}

object Stack {
  def empty[A]: Stack[A] = Stack(Nil)
}

val stack1 = Stack.empty[Int]
val (_, stack2) = stack1.push(1)
val (_, stack3) = stack2.push(2)
val (_, stack4) = stack3.push(3)
val (a1, stack5) = stack4.pop
val (a2, stack6) = stack5.pop
val (a3, stack7) = stack6.pop
assert(a1 == 3)
assert(a2 == 2)
assert(a3 == 1)
