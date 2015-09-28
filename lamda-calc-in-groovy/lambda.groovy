
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



//intに変換する関数
def toInt(n) { n({ it + 1 })(0) }

//booleanに変換する関数
def toBoolean(b) { b(true)(false) }



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



//assert全部通ったら喜んどく
println '･:*+.\\(( °ω° ))/.:+'
