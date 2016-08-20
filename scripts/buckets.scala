
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

type Action = (String, State => State)

case class Step(action: Action, prev: State, next: State) {
  val (name, _) = action
  def contains(state: State) = state == prev || state == next
  override def toString = s"$prev -> $next : $name"
}

case class History(value: List[Step]) {
  def add(step: Step) = History(step :: value)
  def contains(state: State) = value.exists(_.contains(state))
}

object Buckets {
  def actAll(state: State, history: History): Option[History] = {
    val actions = Stream[Action](
      ("Fill A", { case (a, b) => (a.fill, b) }),
      ("Fill B", { case (a, b) => (a, b.fill) }),
      ("Dump A", { case (a, b) => (a.dump, b) }),
      ("Dump B", { case (a, b) => (a, b.dump) }),
      ("Pour from A to B", { case (a, b) => a.pourTo(b) }),
      ("Pour from B to A", { case (a, b) => b.pourTo(a).swap })
    )
    actions.flatMap(act(state, history, _)).headOption
  }

  def act(state: State, history: History, action: Action): Option[History] = {
    val (_, f) = action
    val newState = f(state)
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
    .foldLeft("Initial : " + state) { _ + System.lineSeparator() + _ }
  case _ => "x"
}
println(output)

