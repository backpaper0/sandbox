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
}
