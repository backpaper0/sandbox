
//Zコンビネータ
//Z = λf. (λx. f (λy. x x y)) (λx. f (λy. x x y))
Z = { f -> ({ x -> f({ y -> x(x)(y) }) })({ x -> f({ y -> x(x)(y) }) }) }

//とりあえず数値を定義してみる
ZERO  = { f -> { x -> x }}
ONE   = { f -> { x -> f(x) }}
TWO   = { f -> { x -> f(f(x)) }}
THREE = { f -> { x -> f(f(f(x))) }}

//真偽値
TRUE  = { t -> { f -> t }}
FALSE = { t -> { f -> f }}

//ペア
PAIR  = { l -> { r -> { f -> f(l)(r) }}}
LEFT  = { p -> p(TRUE) }
RIGHT = { p -> p(FALSE) }

//リスト
CONS   = { h -> { t -> PAIR(PAIR(FALSE)(h))(t) }}
NIL    = PAIR(PAIR(TRUE)(TRUE))(TRUE)
IS_NIL = { l -> LEFT(LEFT(l)) }
HEAD   = { l -> RIGHT(LEFT(l)) }
TAIL   = { l -> RIGHT(l) }



//intに変換する関数
def toInt(n) { n({ it + 1 })(0) }

//booleanに変換する関数
def toBoolean(b) { b(true)(false) }

//Listに変換する関数
def toList(l, f) {
    def xs = []
    while(toBoolean(IS_NIL(l)) == false) {
        def h = HEAD(l)
        xs += f(h)
        l = TAIL(l)
    }
    xs
}



//数値
assert(toInt(ZERO)  == 0)
assert(toInt(ONE)   == 1)
assert(toInt(TWO)   == 2)
assert(toInt(THREE) == 3)

//真偽値
assert(toBoolean(TRUE))
assert(toBoolean(FALSE) == false)

//ペア
def p1 = PAIR(ONE)(TWO)
assert(toInt(LEFT(p1))  == 1)
assert(toInt(RIGHT(p1)) == 2)

//リスト
def l1 = CONS(ONE)(CONS(TWO)(CONS(THREE)(NIL)))
assert(toList(l1, { toInt(it) }) == [1, 2, 3 ])



//assert全部通ったら喜んどく
println '･:*+.\\(( °ω° ))/.:+'
