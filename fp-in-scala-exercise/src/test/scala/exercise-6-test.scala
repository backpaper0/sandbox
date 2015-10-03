package exercise6

import org.junit._

class Exercise6Test {

  import RNG._

  @Test def exercise6_1_zero: Unit = {
    assert(nonNegativeInt(MockRNG(0))._1 == 0)
  }
  @Test def exercise6_1_one: Unit = {
    assert(nonNegativeInt(MockRNG(1))._1 == 1)
  }
  @Test def exercise6_1_minus_one: Unit = {
    assert(nonNegativeInt(MockRNG(-1))._1 == 1)
  }
  @Test def exercise6_1_max_value: Unit = {
    assert(nonNegativeInt(MockRNG(Int.MaxValue))._1 == Int.MaxValue)
  }
  @Test def exercise6_1_min_value: Unit = {
    assert(nonNegativeInt(MockRNG(Int.MinValue))._1 == 0)
  }

  @Test def exercise6_2_zero: Unit = {
    assert(double(MockRNG(0))._1 == 0.0)
  }
  @Test def exercise6_2_one: Unit = {
    assert(double(MockRNG(Int.MaxValue))._1 == 1.0)
  }

  @Test def exercise6_4: Unit = {
    val (l, r) = ints(3)(MockRNG(1, 2, 3, 4))
    assert(l == List(1, 2, 3))
    assert(r == MockRNG(4))
  }

  @Test def exercise6_6: Unit = {
    val ra: Rand[Int] = _.nextInt
    val rb: Rand[String] = rng => rng.nextInt match {
      case (b, r) => (b.toString, r)
    }
    val rc = map2(ra, rb)((_, _))
    val (l, r) = rc(MockRNG(1, 2, 3))
    assert(l == (1, "2"))
    assert(r == MockRNG(3))
  }

  case class MockRNG(as: Int*) extends RNG {
    def nextInt: (Int, RNG) = (as.head, MockRNG(as.tail: _*))
  }
}
