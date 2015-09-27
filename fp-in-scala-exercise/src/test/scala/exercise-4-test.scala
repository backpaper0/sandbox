package exercise4

import org.junit._

class Exercise4Test {

  @Test def exercise4_1_some_map: Unit = {
    assert(Some(1).map(_ + 1) == Some(2))
  }
  @Test def exercise4_1_none_map: Unit = {
    assert((None:Option[Int]).map(_ + 1) == None)
  }
  @Test def exercise4_1_some_flatMap_some: Unit = {
    assert(Some(1).flatMap(a => Some(a + 1)) == Some(2))
  }
  @Test def exercise4_1_some_flatMap_none: Unit = {
    assert(Some(1).flatMap(a => None) == None)
  }
  @Test def exercise4_1_none_flatMap: Unit = {
    assert((None:Option[Int]).flatMap(a => Some(a + 1)) == None)
  }
  @Test def exercise4_1_some_getOrElse: Unit = {
    assert(Some(1).getOrElse(2) == 1)
  }
  @Test def exercise4_1_none_getOrElse: Unit = {
    assert((None:Option[Int]).getOrElse(2) == 2)
  }
  @Test def exercise4_1_some_orElse: Unit = {
    assert(Some(1).orElse(Some(2)) == Some(1))
  }
  @Test def exercise4_1_none_orElse: Unit = {
    assert((None:Option[Int]).orElse(Some(2)) == Some(2))
  }
  @Test def exercise4_1_some_filter_true: Unit = {
    assert(Some(1).filter(_ => true) == Some(1))
  }
  @Test def exercise4_1_some_filter_false: Unit = {
    assert(Some(1).filter(_ => false) == None)
  }
  @Test def exercise4_1_none_fitler: Unit = {
    assert((None:Option[Int]).filter(_ => true) == None)
  }

  @Test def exercise4_3_some_some: Unit = {
    assert(Option.map2(Some(1), Some(2))(_ + _) == Some(3))
  }
  @Test def exercise4_3_some_none: Unit = {
    assert(Option.map2(Some(1), None:Option[Int])(_ + _) == None)
  }
  @Test def exercise4_3_none_some: Unit = {
    assert(Option.map2(None:Option[Int], Some(2))(_ + _) == None)
  }

  @Test def exercise4_4_all_some: Unit = {
    assert(Option.sequence(List(Some(1), Some(2), Some(3))) == Some(List(1, 2, 3)))
  }
  @Test def exercise4_4_has_none: Unit = {
    assert(Option.sequence(List(Some(1), None, Some(3))) == None)
  }
}
