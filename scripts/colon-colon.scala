
//Intのペアを表現する :::::::: を定義して説明してみる

class ::::::::(val x: Int, val y: Int)

//コンパニオンオブジェクト
object :::::::: {
  //Some((x, y))を返すunapplyメソッドがあれば中置記法が使える
  def unapply(a: ::::::::) = Some((a.x, a.y))
}

val a = new ::::::::(123, 456)

//中置記法
val b = a match {
  case x :::::::: _ => x
}

//中置記法を使わない場合はこんな感じ
val c = a match {
  case ::::::::(_, y) => y
}

println(b)
println(c)

