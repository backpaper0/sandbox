package study

import org.scalatest._
import org.scalatest.junit._
import org.junit.runner.RunWith
import study.implicits._

@RunWith(classOf[JUnitRunner])
class FunctorSpec extends FlatSpec with Matchers {

  "Just(2) * 2" should "be Just(4)" in {
    val a: Maybe[Int] = (2).point[Maybe]
    val f: Int => Int = _ * 2
    a.map(f) should be (Just(4))
  }

  "Nothing * 2" should "be Nothing" in {
    val a: Maybe[Int] = Nothing.asInstanceOf[Maybe[Int]]
    val f: Int => Int = _ * 2
    a.map(f) should be (Nothing)
  }

  "Just(2) ap Just(* 2)" should "be Just(4)" in {
    val a: Maybe[Int] = (2).point[Maybe]
    val f: Maybe[Int => Int] = ((_: Int) * 2).point[Maybe]
    a.ap(f) should be (Just(4))
  }

  "Nothing ap Just(* 2)" should "be Nothing" in {
    val a: Maybe[Int] = Nothing.asInstanceOf[Maybe[Int]]
    val f: Maybe[Int => Int] = ((_: Int) * 2).point[Maybe]
    a.ap(f) should be (Nothing)
  }

  "Just(2) ap Nothing" should "be Nothing" in {
    val a: Maybe[Int] = Nothing.asInstanceOf[Maybe[Int]]
    val f: Maybe[Int => Int] = Nothing.asInstanceOf[Maybe[Int => Int]]
    a.ap(f) should be (Nothing)
  }
  
  "List(1, 2, 3) * 2" should "be List(2, 4, 6)" in {
    val a: List[Int] = Cons(1, Cons(2, Cons(3, Nil.asInstanceOf[List[Int]])))
    val f: Int => Int = _ * 2
    a.map(f) should be (Cons(2, Cons(4, Cons(6, Nil.asInstanceOf[List[Int]]))))
  }

  "Nil * 2" should "Nil" in {
    val a: List[Int] = Nil.asInstanceOf[List[Int]]
    val f: Int => Int = _ * 2
    a.map(f) should be (Nil.asInstanceOf[List[Int]])
  }

  "List(1, 2, 3) ap List(* 2, + 1)" should "be List(2, 4, 6, 2, 3, 4)" in {
    val a: List[Int] = Cons(1, Cons(2, Cons(3, Nil.asInstanceOf[List[Int]])))
    val f: List[Int => Int] = Cons(_ * 2, Cons(_ + 1, Nil.asInstanceOf[List[Int => Int]]))
    a.ap(f) should be (Cons(2, Cons(4, Cons(6, Cons(2, Cons(3, Cons(4, Nil.asInstanceOf[List[Int]])))))))
  }

  "Just(2) map id" should "Just(2)" in {
    val a: Maybe[Int] = Just(2)
    val id: Int => Int = x => x
    a.map(id) should be (a)
  }

  "Just(2) map f map g" should "Just(2) map f andThen g" in {
    val a: Maybe[Int] = Just(2)
    val f: Int => Int = x => x * 2
    val g: Int => String = x => "*" + x + "*"
    a.map(f).map(g) should be (a.map(f andThen g))
  }

  "Int => String map String => Array[Char]" should "Int => Array[Char]" in {
    val a: Int => String = _.toString
    val f: String => Array[Char] = _.toCharArray
    functionFunctor[Int].map(a, f)(123) should be (Array('1', '2', '3'))
  }

}
