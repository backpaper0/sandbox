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

  @Test def exercise_3_4 = {
    val xs = List(1, 2, 3, 4, 5)
    val ys = List.drop(xs, 3)
    assert(ys == List(4, 5))
  }

  @Test def exercise_3_5 = {
    val xs = List(1, 2, 3, 4, 5)
    val ys = List.dropWhile(xs)(_ < 3)
    assert(ys == List(3, 4, 5))
  }

  @Test def exercise_3_6 = {
    val xs = List(1, 2, 3, 4)
    val ys = List.init(xs)
    assert(ys == List(1, 2, 3))
  }

  @Test def exercise_3_8 = {
    val xs = List(1, 2, 3)
    val ys = List.foldRight(xs, Nil: List[Int])(Cons(_, _))
    assert(ys == List(1, 2, 3))
  }

  @Test def exercise_3_10 = {
    val xs = List(1, 2, 3, 4)
    val ys = List.foldLeft(xs, 0)(_ + _)
    assert(ys == 10)
  }

  @Test def exercise_3_11_sum = {
    val xs = List(1, 2, 3, 4)
    val ys = List.sum(xs)
    assert(ys == 10)
  }

  @Test def exercise_3_11_product = {
    val xs = List(2.0, 3.0, 4.0)
    val ys = List.product(xs)
    assert(ys == 24.0)
  }

  @Test def exercise_3_11_length = {
    val xs = List(1, 2, 3, 4, 5)
    val ys = List.length(xs)
    assert(ys == 5)
  }

  @Test def exercise_3_12 = {
    val xs = List(1, 2, 3, 4, 5)
    val ys = List.reverse(xs)
    assert(ys == List(5, 4, 3, 2, 1))
  }

  @Test def exercise_3_14 = {
    val xs = List(1, 2, 3)
    val ys = List(4, 5, 6)
    val zs = List.append(xs, ys)
    assert(zs == List(1, 2, 3, 4, 5, 6))
  }

  @Test def exercise_3_16 = {
    val xs = List(1, 2, 3)
    val ys = List.increment(xs)
    assert(ys == List(2, 3, 4))
  }

  @Test def exercise_3_17 = {
    val xs = List(1.0, 2.0, 3.0)
    val ys = List.doubleToString(xs)
    assert(ys == List("1.0", "2.0", "3.0"))
  }

  @Test def exercise_3_19 = {
    val xs = List(1, 2, 3, 4, 5, 6)
    val ys = List.filter(xs)(_ % 2 == 0)
    assert(ys == List(2, 4, 6))
  }

  @Test def exercise_3_20 = {
    val xs = List(1, 2, 3)
    val ys = List.flatMap(xs)(i => List(i, i))
    assert(ys == List(1, 1, 2, 2, 3, 3), ys)
  }

  @Test def exercise_3_22 = {
    val xs = List(1, 2, 3)
    val ys = List(4, 5, 6)
    val zs = List.sum(xs, ys)
    assert(zs == List(5, 7, 9))
  }

  @Test def exercise_3_22b = {
    val xs = List(1, 2, 3, 4)
    val ys = List(4, 5, 6)
    val zs = List.sum(xs, ys)
    assert(zs == List(5, 7, 9))
  }

  @Test def exercise_3_22c = {
    val xs = List(1, 2, 3)
    val ys = List(4, 5, 6, 7)
    val zs = List.sum(xs, ys)
    assert(zs == List(5, 7, 9))
  }

  @Test def exercise_3_25 = {
    val t = Branch(Leaf(1), Branch(Leaf(2), Branch(Leaf(3), Leaf(4))))
    val u = Tree.size(t)
    assert(u == 7)
  }

  @Test def exercise_3_26 = {
    val t = Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Branch(Branch(Leaf(4), Leaf(5)), Leaf(6))))
    val u = Tree.maximum(t)
    assert(u == 6)
  }

  @Test def exercise_3_27 = {
    val l = Leaf(0)
    val t = Branch(Branch(Branch(Branch(l, l), l), Branch(l, l)), Branch(l, Branch(l, l)))
    val u = Tree.depth(t)
    assert(u == 5)
  }

  @Test def exercise_3_28 = {
    val t = Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4)))
    val u = Tree.map(t)(_ * 2)
    assert(u == Branch(Branch(Leaf(2), Leaf(4)), Branch(Leaf(6), Leaf(8))))
  }
}
