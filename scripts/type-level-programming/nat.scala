import scala.language.higherKinds

sealed trait Bool {
  type If[Then <: A, Else <: A, A] <: A
}
sealed trait True extends Bool {
  type If[Then <: A, Else <: A, A] = Then
}
sealed trait False extends Bool {
  type If[Then <: A, Else <: A, A] = Else
}

object Bool {
  type If[Cond <: Bool, Then <: A, Else <: A, A] = Cond#If[Then, Else, A]
}

import Bool._

sealed trait Nat {
  type increment <: Nat
  type decrement <: Nat
  type add[A <: Nat] <: Nat
  type mul[A <: Nat] <: Nat
  type sub[A <: Nat] <: Nat
  type isZero <: Bool
}

sealed trait _0 extends Nat {
  type increment = succ[_0]
  type decrement = _0
  type add[A <: Nat] = A
  type mul[A <: Nat] = _0
  type sub[A <: Nat] = _0
  type isZero = True
}

sealed trait succ[A <: Nat] extends Nat {
  type increment = succ[succ[A]]
  type decrement = A
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
  type sub[B <: Nat] = If[B#isZero, succ[A], A#sub[B#decrement], Nat]
  type isZero = False
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
  type -[A <: Nat, B <: Nat] = A#sub[B]
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

println("3 - 2 = " + toInt[_3 - _2])
println("5 - 1 = " + toInt[_5 - _1])

