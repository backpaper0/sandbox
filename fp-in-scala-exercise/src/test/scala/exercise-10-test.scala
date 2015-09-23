package exercise_10

import org.junit._
import org.junit.Assert._

class MonoidSpec {

  import monoid._

  @Test
  def testIntAddition() {
    testMonoidLaw(intAddition, 2, 3, 4)
  }

  @Test
  def testIntMultiplication() {
    testMonoidLaw(intMultiplication, 2, 3, 4)
  }

  @Test
  def testBooeanOr() {
    testMonoidLaw(booleanOr, true, false, true)
  }

  @Test
  def testBooleanAnd() {
    testMonoidLaw(booleanAnd, true, false, true)
  }

  @Test
  def testOptionMonoid() {
    testMonoidLaw(optionMonoid(intAddition), Some(2), Some(3), Some(4))
  }

  private def testMonoidLaw[A](m: Monoid[A], a1: A, a2: A, a3: A) {
    assertTrue(m.op(m.op(a1, a2), a3) == m.op(a1, m.op(a2, a3)))
    List(a1, a2, a3).foreach { a =>
      assertTrue(m.op(a, m.zero) == a)
      assertTrue(a == m.op(a, m.zero))
    }
  }
}
