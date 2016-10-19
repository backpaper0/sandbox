import scala.language.higherKinds

sealed trait Bool

sealed trait True extends Bool {
  type Not = False
}

sealed trait False extends Bool {
  type Not = True
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

println(ToBool[True])
println(ToBool[False])

println(ToBool[True#Not])
println(ToBool[False#Not])
