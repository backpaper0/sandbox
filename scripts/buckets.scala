
case class Bucket(name: String, capacity: Int, value: Int) {
  private val free = capacity - value
  val satisfied = value == 4
  def fill = copy(value = capacity)
  def dump = copy(value = 0)
  def pourTo(other: Bucket) = {
    val x = Math.min(other.free, value)
    (add(0 - x), other.add(x))
  }
  private def add(addMe: Int) = copy(value = value + addMe)
  override def toString = s"$name($value/$capacity)"
}

type State = (Bucket, Bucket)

case class Action(name: String, f: State => State) {
  def apply(state: State) = f(state)
  override def toString = name
}

case class Step(action: Action, prev: State, next: State) {
  def contains(state: State) = state == prev || state == next
  override def toString = s"$prev -> $next : $action"
}

case class History(value: List[Step]) {
  def add(step: Step) = History(step :: value)
  def contains(state: State) = value.exists(step => step.contains(state))
}

object Buckets {
  def actAll(state: State, history: History): Option[History] = {
    val actions = Stream(
      Action("Fill A", { case (a, b) => (a.fill, b) }),
      Action("Fill B", { case (a, b) => (a, b.fill) }),
      Action("Dump A", { case (a, b) => (a.dump, b) }),
      Action("Dump B", { case (a, b) => (a, b.dump) }),
      Action("Pour from A to B", { case (a, b) => a.pourTo(b) }),
      Action("Pour from B to A", { case (a, b) => b.pourTo(a).swap })
    )
    actions.flatMap(action => act(state, history, action)).headOption
  }

  def act(state: State, history: History, action: Action): Option[History] = {
    val newState = action(state)
    val newHistory = history.add(Step(action, state, newState))
    if (satisfied(newState))
      Option(newHistory)
    else if (history.contains(newState))
      Option.empty
    else
      actAll(newState, newHistory)
  }

  def satisfied(state: State) = state match {
    case (a, b) => a.satisfied || b.satisfied
  }
}

val state = (Bucket("A", 5, 0), Bucket("B", 3, 0))
val history = History(List.empty)
val result = Buckets.actAll(state, history)

val output = result match {
  case Some(History(value)) => value
    .reverse
    .foldLeft("Initial : " + state) { (x, y) => x + System.lineSeparator() + y }
  case _ => "x"
}
println(output)

