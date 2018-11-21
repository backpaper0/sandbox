import scala.util.Try

// sealed abstract class MyException
// 
// case class BadException extends MyException

def doSomething1(): Try[String] = Try("foo")

def doSomething2(): Try[String] = Try("bar")

val ret = for {
  ret1 <- doSomething1()
  ret2 <- doSomething2()
} yield ret1 + ret2

assert(ret == Try("foobar"))
