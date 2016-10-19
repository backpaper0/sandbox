import scala.language.higherKinds

sealed trait Bool {
  type Not <: Bool
  type And[A <: Bool] <: Bool
  type Or[A <: Bool] <: Bool
}

sealed trait True extends Bool {
  type Not = False
  type And[A <: Bool] = A
  type Or[A <: Bool] = True
}

sealed trait False extends Bool {
  type Not = True
  type And[A <: Bool] = False
  type Or[A <: Bool] = A
}

object Bool {
  type ![A <: Bool] = A#Not
  type &&[A <: Bool, B <: Bool] = A#And[B]
  type ||[A <: Bool, B <: Bool] = A#Or[B]
}

object ToBool {
  def apply[A <: Bool](implicit x: ToBool[A]) = x()
}

trait ToBool[A <: Bool] {
  def apply(): Boolean
}

implicit val toTrue = new ToBool[True] {
  def apply() = true
}

implicit val toFalse = new ToBool[False] {
  def apply() = false
}

import Bool._

println(ToBool[True])
println(ToBool[False])

//println(ToBool[True#Not])
//println(ToBool[False#Not])

println("!true  = " + ToBool[![True]])
println("!false = " + ToBool[![False]])

//println("true  && true  = " + ToBool[True#And[True]])
//println("true  && false = " + ToBool[True#And[False]])
//println("false && true  = " + ToBool[False#And[True]])
//println("false && false = " + ToBool[False#And[False]])

println("true  && true  = " + ToBool[True  && True])
println("true  && false = " + ToBool[True  && False])
println("false && true  = " + ToBool[False && True])
println("false && false = " + ToBool[False && False])

//println("true  || true  = " + ToBool[True#Or[True]])
//println("true  || false = " + ToBool[True#Or[False]])
//println("false || true  = " + ToBool[False#Or[True]])
//println("false || false = " + ToBool[False#Or[False]])

println("true  || true  = " + ToBool[True  || True])
println("true  || false = " + ToBool[True  || False])
println("false || true  = " + ToBool[False || True])
println("false || false = " + ToBool[False || False])

