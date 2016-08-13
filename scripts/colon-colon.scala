object :::::::: {
  def unapply[A](xs: ::[A]): Some[(A, List[A])] = Some((xs.head, xs.tail))
}

val xs = 1 to 5 toList

val hoge = xs match {
  case h :::::::: _ => h
  case _ => -1
}

println(hoge)

