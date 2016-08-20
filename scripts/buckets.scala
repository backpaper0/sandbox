val capacityA = 5
val capacityB = 3
val target = 4

case class Bucket(name: String, capacity: Int, value: Int) {
  private val free = capacity - value
  val satisfied = value == target
  def fill = copy(value = capacity)
  def dump = copy(value = 0)
  def pourTo(other: Bucket) = {
    val x = Math.min(other.free, value)
    (add(0 - x), other.add(x))
  }
  private def add(addMe: Int) = copy(value = value + addMe)
  override def toString = s"$name($value)"
}

type State = (Bucket, Bucket)

type Action = (String, State => State)

case class Step(name: String, state: State) {
  private val (a, b) = state
  def contains(s: State) = s == state
  override def toString = s"$name : $a $b"
}

case class History(value: List[Step]) {
  def add(step: Step) = History(step :: value)
  def contains(state: State) = value.exists(_.contains(state))
}

object Buckets {
  def actAll(state: State, history: History): Option[History] = {
    val actions = Stream[Action](
      (" Fill A", { case (a, b) => (a.fill, b) }),
      (" Fill B", { case (a, b) => (a, b.fill) }),
      (" Dump A", { case (a, b) => (a.dump, b) }),
      (" Dump B", { case (a, b) => (a, b.dump) }),
      (" A -> B", { case (a, b) => a.pourTo(b) }),
      (" A <- B", { case (a, b) => b.pourTo(a).swap })
    )
    actions.flatMap(act(state, history, _)).headOption
  }

  def act(state: State, history: History, action: Action): Option[History] = {
    val (name, f) = action
    val newState = f(state)
    val newHistory = history.add(Step(name, newState))
    if (satisfied(newState))
      Option(newHistory)
    else if (history.contains(newState))
      Option.empty
    else
      actAll(newState, newHistory)
  }

  private def satisfied(state: State) = state match {
    case (a, b) => a.satisfied || b.satisfied
  }
}

val state = (Bucket("A", capacityA, 0), Bucket("B", capacityB, 0))
val history = History(List(Step("   Init", state)))
val result = Buckets.actAll(state, history)

val header = state match {
  case (a, b) => s"Buckets : ${a.fill} ${b.fill}"
}
val border = "--------:----------"
val output = result match {
  case Some(History(value)) => value
    .reverse
    .map(_.toString)
    .reduce(_ + System.lineSeparator() + _)
  case _ => "x"
}
println(header)
println(border)
println(output)

