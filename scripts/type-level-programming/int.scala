import scala.language.higherKinds

sealed trait int

sealed trait _0 extends int

sealed trait succ[A <: int] extends int

type _1 = succ[_0]

type _2 = succ[_1]

type _3 = succ[_2]

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

println(toInt[_0])
println(toInt[_1])
println(toInt[_2])
println(toInt[_3])
