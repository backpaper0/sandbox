
sealed trait Term

case class TmTrue(info: String) extends Term

case class TmFalse(info: String) extends Term

case class TmIf(info: String, term1: Term, term2: Term, term3: Term) extends Term

case class TmZero(info: String) extends Term

case class TmSucc(info: String, term: Term) extends Term

case class TmPred(info: String, term: Term) extends Term

case class TmIsZero(info: String, term: Term) extends Term

def isnumericval(t: Term): Boolean = t match {
  case TmZero(_) => true
  case TmSucc(_, t1) => isnumericval(t1)
  case _ => false
}

def isval(t: Term): Boolean = t match {
  case TmTrue(_) => true
  case TmFalse(_) => true
  case _ if(isnumericval(t)) => true
  case _ => false
}

class NoRuleApplies extends Exception

val dummyinfo = "dummyinfo"

def eval1(t: Term): Term = t match {
  case TmIf(_, TmTrue(_), t2, t3) => t2
  case TmIf(_, TmFalse(_), t2, t3) => t3
  case TmIf(fi, t1, t2, t3) => TmIf(fi, eval1(t1), t2, t3)
  case TmSucc(fi, t1) => TmSucc(fi, eval1(t1))
  case TmPred(_, TmZero(_)) => TmZero(dummyinfo)
  case TmPred(_, TmSucc(_, nv1)) if (isnumericval(nv1)) => nv1
  case TmPred(fi, t1) => TmPred(fi, eval1(t1))
  case TmIsZero(_, TmZero(_)) => TmTrue(dummyinfo)
  case TmIsZero(_, TmSucc(_, nv1)) if (isnumericval(nv1)) => TmFalse(dummyinfo)
  case TmIsZero(fi, t1) => TmIsZero(fi, eval1(t1))
  case _ => throw new NoRuleApplies()
}

def eval(t: Term): Term = try {
  eval(eval1(t))
} catch {
  case e: NoRuleApplies => t
}

//---- example ----

def evalAndPrintln(t: Term) {
  println(s"eval ${t} =")
  println(eval(t))
  println()
}
evalAndPrintln(TmTrue("a"))
evalAndPrintln(TmSucc("a", TmPred("b", TmSucc("c", TmZero("d")))))
evalAndPrintln(TmIf("a", TmIsZero("b", TmPred("c", TmSucc("d", TmZero("e")))), TmZero("f"), TmSucc("g", TmZero("h"))))

