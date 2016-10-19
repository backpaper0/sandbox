import scala.language.higherKinds

sealed trait int {
  type increment <: int
  type add[A <: int] <: int
}

sealed trait _0 extends int {
  type increment = succ[_0]
  type add[A <: int] = A
}

sealed trait succ[A <: int] extends int {
  type increment = succ[succ[A]]
  type add[B <: int] = B#add[A]#increment
  // _2#add[_1]
  // succ[_1]#add[_1]
  // _1#add[_1]#increment
  // succ[_0]#add[_1]#increment
  // _1#add[_0]#increment#increment
  // succ[_0]#add[_0]#increment#increment
  // _0#add[_0]#increment#increment#increment
  // _0#increment#increment#increment
  // _3
}

type _1 = succ[_0]

type _2 = succ[_1]

type _3 = succ[_2]

object int {
  type ++[A <: int] = A#increment
  type +[A <: int, B <: int] = A#add[B]
}

def toInt[A <: int](implicit x: ToInt[A]) = x()

sealed trait ToInt[A <: int] {
  def apply(): Int
}

implicit def to0 = new ToInt[_0] {
  def apply() = 0
}

implicit def toSucc[B <: int](implicit x: ToInt[B]) = new ToInt[succ[B]] {
  def apply() = 1 + x()
}

import int._

println(toInt[_0])
println(toInt[_1])
println(toInt[_2])
println(toInt[_3])

println("++3 = " + toInt[++[_3]])

println("2 + 3 = " + toInt[_2 + _3])
