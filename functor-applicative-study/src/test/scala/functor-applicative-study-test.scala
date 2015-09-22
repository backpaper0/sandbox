package study

import org.scalatest._
import org.scalatest.junit._
import org.junit.runner.RunWith
import study.Functors._

@RunWith(classOf[JUnitRunner])
class FunctorSpec extends FlatSpec with Matchers {

  "Just(2) * 4" should "be Just(4)" in {
    val a: Maybe[Int] = Just(2)
    a.map(_ * 2) should be (Just(4))
  }

  "Nothing * 4" should "be Nothing" in {
    val a: Maybe[Int] = Nothing.asInstanceOf[Maybe[Int]]
    a.map(_ * 2) should be (Nothing)
  }

  "List(1, 2, 3) * 4" should "be List(2, 4, 6)" in {
    val a: List[Int] = Cons(1, Cons(2, Cons(3, Nil.asInstanceOf[List[Int]])))
    a.map(_ * 2) should be (Cons(2, Cons(4, Cons(6, Nil.asInstanceOf[List[Int]]))))
  }

  "Nil * 4" should "Nil" in {
    val a: List[Int] = Nil.asInstanceOf[List[Int]]
    a.map(_ * 2) should be (Nil.asInstanceOf[List[Int]])
  }

  "Just(2) map id" should "Just(2)" in {
    val a: Maybe[Int] = Just(2)
    a.map(x => x) should be (a)
  }

  "Just(2) map f map g" should "Just(2) map f andThen g" in {
    val a: Maybe[Int] = Just(2)
    val f: Int => Int = x => x * 2
    val g: Int => String = x => "*" + x + "*"
    a.map(f).map(g) should be (a.map(f andThen g))
  }
}
