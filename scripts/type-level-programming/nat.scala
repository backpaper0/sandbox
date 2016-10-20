import scala.language.higherKinds

sealed trait Nat {
  type increment <: Nat
  type add[A <: Nat] <: Nat
  type mul[A <: Nat] <: Nat
}

sealed trait _0 extends Nat {
  type increment = succ[_0]
  type add[A <: Nat] = A
  type mul[A <: Nat] = _0
}

sealed trait succ[A <: Nat] extends Nat {
  type increment = succ[succ[A]]
  type add[B <: Nat] = B#add[A]#increment
  // _2#add[_1]
  // succ[_1]#add[_1]
  // _1#add[_1]#increment
  // succ[_0]#add[_1]#increment
  // _1#add[_0]#increment#increment
  // succ[_0]#add[_0]#increment#increment
  // _0#add[_0]#increment#increment#increment
  // _0#increment#increment#increment
  // _3
  type mul[B <: Nat] = A#mul[B]#add[B]
}

type _1 = succ[_0]
type _2 = succ[_1]
type _3 = succ[_2]
type _4 = succ[_3]
type _5 = succ[_4]

object Nat {
  type ++[A <: Nat] = A#increment
  type +[A <: Nat, B <: Nat] = A#add[B]
  type *[A <: Nat, B <: Nat] = A#mul[B]
}

def toInt[A <: Nat](implicit x: ToInt[A]) = x()

sealed trait ToInt[A <: Nat] {
  def apply(): Int
}

implicit def to0 = new ToInt[_0] {
  def apply() = 0
}

implicit def toSucc[B <: Nat](implicit x: ToInt[B]) = new ToInt[succ[B]] {
  def apply() = 1 + x()
}

import Nat._

println(toInt[_0])
println(toInt[_1])
println(toInt[_2])
println(toInt[_3])

println("++3 = " + toInt[++[_3]])

println("2 + 3 = " + toInt[_2 + _3])

println("2 * 3 = " + toInt[_2 * _3])

println("1 * 2 * 3 * 4 * 5 = " + toInt[_1 * _2 * _3 * _4 * _5])
