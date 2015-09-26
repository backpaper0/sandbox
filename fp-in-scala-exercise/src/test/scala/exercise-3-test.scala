package exercise3

import org.junit._

class Exercise3Test {
  @Test def exercise3_1 = {
    val x = List(1, 2, 3, 4, 5) match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case Nil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + List.sum(t)
      case _ => 101
    }
    assert(x == 3)
  }

  @Test def exercise_3_2 = {
    val xs = List(1, 2, 3)
    val ys = List.tail(xs)
    assert(ys == List(2, 3))
  }
  @Test def exercise_3_2_Nil = {
    assert(List.tail(Nil) == Nil)
  }

  @Test def exercise_3_3 = {
    val xs = List(1, 2, 3)
    val ys = List.setHead(xs, 4)
    assert(ys == List(4, 2, 3))
  }
}
