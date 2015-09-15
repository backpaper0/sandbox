package study

import org.scalatest._
import org.scalatest.junit._
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class FunctorSpec extends FlatSpec with Matchers {

  "Just(2) * 4" should "be Just(4)" in {
    val a: Maybe[Int] = Just(2)
    a.fmap(_ * 2) should be (Just(4))
  }

  "Nothing * 4" should "be Nothing" in {
    val a: Maybe[Int] = Nothing()
    a.fmap(_ * 2) should be (Nothing())
  }

  "List(1, 2, 3) * 4" should "be List(2, 4, 6)" in {
    val a: List[Int] = Cons(1, Cons(2, Cons(3, Nil())))
    a.fmap(_ * 2) should be (Cons(2, Cons(4, Cons(6, Nil()))))
  }

  "Nil * 4" should "Nil" in {
    val a: List[Int] = Nil()
    a.fmap(_ * 2) should be (Nil())
  }
}
