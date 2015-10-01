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
}
