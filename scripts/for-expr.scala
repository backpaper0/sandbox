// http://www.scala-lang.org/files/archive/spec/2.11/06-expressions.html#for-comprehensions-and-for-loops

//oa map(a => a.toString)
def likeMap(oa: Option[Int]): Option[String] =
  for {
    a <- oa
  } yield a.toString


//oa flatMap(a => ob map(b => a + b))
def likeFlatMapAndMap(oa: Option[Int], ob: Option[Int]): Option[Int] =
  for {
    a <- oa
    b <- ob
  } yield a + b

//as foreach(a => println(a))
def likeForeach(as: List[Int]): Unit =
  for {
    a <- as
  } println(a)

//as withFilter(a => a % 2 == 0) map (a => a.toString)
def likeWithFilterAndMap(as: List[Int]): List[String] =
  for {
    a <- as if a % 2 == 0
  } yield a.toString
