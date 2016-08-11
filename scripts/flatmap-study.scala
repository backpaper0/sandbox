//scalc -Xlog-implicit-conversions flatmap-study.scala
//
//flatmap-study.scala:3: inferred view from Some[String] to scala.collection.GenTraversableOnce[?] = scala.this.Option.option2Iterable[String]:(xo: Option[String])Iterable[String]
//    xs.flatMap(x => Some(x))
//
class Hoge {
  def test(xs: List[String]): List[String] = {
    xs.flatMap(x => Some(x))
  }
}
