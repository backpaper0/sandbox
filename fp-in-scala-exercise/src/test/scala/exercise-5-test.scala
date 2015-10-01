package exercise5

import org.junit._

class Exercise5Test {

  @Test def exercise5_1: Unit = {
    assert(Stream(1, 2, 3).toList == List(1, 2, 3))
  }
}
