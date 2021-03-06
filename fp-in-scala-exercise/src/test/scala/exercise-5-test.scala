package exercise5

import org.junit._

class Exercise5Test {

  @Test def exercise5_1: Unit = {
    assert(Stream(1, 2, 3).toList == List(1, 2, 3))
  }

  @Test def exercise5_2_take: Unit = {
    assert(Stream(1, 2, 3).take(2).toList == List(1, 2))
  }

  @Test def exercise5_2_drop: Unit = {
    assert(Stream(1, 2, 3).drop(1).toList == List(2, 3))
  }

  @Test def exercise5_3: Unit = {
    assert(Stream(1, 2, 3, 4, 5).dropWhile(_ < 3).toList == List(3, 4, 5))
  }

  @Test def exercise5_4: Unit = {
    assert(Stream(1, 2, 3).forAll(_ < 5))
  }
  @Test def exercise5_4_false: Unit = {
    assert(Stream(() => 5, () => sys.error(""), () => 1).forAll(_() < 5) == false)
  }

  @Test def exercise5_5: Unit = {
    assert(Stream(1, 2, 3, 4, 3).takeWhile(_ < 4).toList == List(1, 2, 3))
  }

  @Test def exercise5_6: Unit = {
    assert(Stream(1, 2, 3).headOption == Some(1))
  }
  @Test def exercise5_6_empty: Unit = {
    assert(Stream.empty.headOption == None)
  }

  @Test def exercise5_7_map: Unit = {
    assert(Stream(1, 2, 3).map(_ * 2).toList == List(2, 4, 6))
  }
  @Test def exercise5_7_filter: Unit = {
    assert(Stream(1, 2, 3).filter(_ % 2 == 0).toList == List(2))
  }
  @Test def exercise5_7_append: Unit = {
    assert(Stream(1, 2, 3).append(Stream(4, 5)).toList == List(1, 2, 3, 4, 5))
  }
  @Test def exercise5_7_append_lazy: Unit = {
    assert(Stream(() => 1).append(Stream(() => sys.error(""))).headOption.map(a => a()) == Some(1))
  }
  @Test def exercise5_7_flatMap: Unit = {
    assert(Stream(1, 2, 3).flatMap(a => Stream(a, a)).toList == List(1, 1, 2, 2, 3, 3))
  }

  @Test def exercise5_8: Unit = {
    assert(Stream.constant(9).take(3).toList == List(9, 9, 9))
  }

  @Test def exercise5_9: Unit = {
    assert(Stream.from(3).take(3).toList == List(3, 4, 5))
  }

  @Test def exercise5_10: Unit = {
    assert(Stream.fibs.take(11).toList == List(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55))
  }

  @Test def exercise5_11: Unit = {
    assert(Stream.unfold(1)(s => if (s < 4) Some((s, s + 1)) else None).toList == List(1, 2, 3))
  }

  @Test def exercise5_12_ones: Unit = {
    assert(Stream.ones.take(3).toList == List(1, 1, 1))
  }

  @Test def exercise5_13_zipWith: Unit = {
    assert(Stream.from(1).zipWith(Stream(2, 4, 6))(_ + _).toList == List(3, 6, 9))
  }
  @Test def exercise5_13_zipAll: Unit = {
    assert(Stream.ones.zipAll(Stream(1, 2)).take(3).toList == List((Some(1), Some(1)), (Some(1), Some(2)), (Some(1), None)))
  }

  @Test def exercise5_14_true: Unit = {
    assert(Stream.from(1).startsWith(Stream(1, 2, 3)))
  }
  @Test def exercise5_14_false: Unit = {
    assert(Stream.from(0).startsWith(Stream.fibs) == false)
  }

  @Test def exercise5_15: Unit = {
    assert(Stream(1, 2, 3).tails.toList.map(_.toList) == List(List(1, 2, 3), List(2, 3), List(3), Nil))
  }
}
