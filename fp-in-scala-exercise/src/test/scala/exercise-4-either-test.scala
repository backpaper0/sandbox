package exercise4

import org.junit._

class Exercise4EitherTest {

  @Test def exercise4_6_right_map: Unit = {
    assert(Right(1).map(_.toString) == Right("1"))
  }
  @Test def exercise4_6_left_map: Unit = {
    assert(Left(1).map(_.toString) == Left(1))
  }
  @Test def exercise4_6_right_flatMap_right: Unit = {
    assert(Right(1).flatMap(r => Right(r.toString)) == Right("1"))
  }
  @Test def exercise4_6_right_flatMap_left: Unit = {
    assert(Right(1).flatMap(_ => Left(0)) == Left(0))
  }
  @Test def exercise4_6_left_flatMap: Unit = {
    assert(Left(1).flatMap(r => Right(r.toString)) == Left(1))
  }
  @Test def exercise4_6_right_orElse: Unit = {
    assert(Right(1).orElse(Right(2)) == Right(1))
  }
  @Test def exercise4_6_left_orElse: Unit = {
    assert(Left(1).orElse(Right(2)) == Right(2))
  }
  @Test def exercise4_6_right_map2_right: Unit = {
    assert(Right(1).map2(Right(2))(_ + _) == Right(3))
  }
  @Test def exercise4_6_left_map2_right: Unit = {
    assert((Left(1):Either[Int, Int]).map2(Right(2))(_ + _) == Left(1))
  }
  @Test def exercise4_6_right_map2_left: Unit = {
    assert(Right(1).map2(Left(2))(_ + _) == Left(2))
  }
  @Test def exercise4_6_left_map2_left: Unit = {
    assert((Left(1):Either[Int, Int]).map2(Left(2))(_ + _) == Left(1))
  }

  @Test def exercise4_7_sequence_all_right: Unit = {
    assert(Either.sequence(List(Right(1), Right(2), Right(3))) == Right(List(1, 2, 3)))
  }
  @Test def exercise4_7_sequence_has_left: Unit = {
    assert(Either.sequence(List(Right(1), Left(2), Left(3))) == Left(2))
  }
  @Test def exercise4_7_sequence_nil: Unit = {
    assert(Either.sequence(Nil) == Right(Nil))
  }
  @Test def exercise4_7_traverse_all_right: Unit = {
    assert(Either.traverse(List(1, 2, 3))(a => Right(a.toString)) == Right(List("1", "2", "3")))
  }
  @Test def exercise4_7_traverse_has_left: Unit = {
    assert(Either.traverse(List(1, 2, 3))(a => if(a % 2 == 0) Right(a.toString) else Left(a)) == Left(1))
  }
  @Test def exercise4_7_traverse_nil: Unit = {
    assert(Either.traverse(Nil)(Right(_)) == Right(Nil))
  }
}
