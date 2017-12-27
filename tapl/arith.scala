
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

def eval1(t: Term): Term = ???

