
//Zコンビネータ
//Z = λf. (λx. f (λy. x x y)) (λx. f (λy. x x y))
Z = { f -> ({ x -> f({ y -> x(x)(y) }) })({ x -> f({ y -> x(x)(y) }) }) }

//とりあえず数値を定義してみる
ZERO  = { f -> { x -> x }}
ONE   = { f -> { x -> f(x) }}
TWO   = { f -> { x -> f(f(x)) }}
THREE = { f -> { x -> f(f(f(x))) }}
FOUR  = { f -> { x -> f(f(f(f(x)))) }}
FIVE  = { f -> { x -> f(f(f(f(f(x))))) }}

//数値関数いろいろ
SUCC = { n -> { f -> { x -> f(n(f)(x)) }}}
ADD  = { m -> { n -> { f -> { x -> m(f)(n(f)(x)) }}}}
PRED = { n -> LEFT(n({ x -> PAIR(RIGHT(x))(SUCC(RIGHT(x))) })(PAIR(ZERO)(ZERO))) }
SUB  = { m -> { n -> n(PRED)(m) }}
MUL  = { m -> { n -> n(ADD(m))(ZERO) }}

IS_ZERO = { n -> n({ x -> FALSE })(TRUE) }

//真偽値
TRUE  = { t -> { f -> t }}
FALSE = { t -> { f -> f }}
IF    = { b -> { x -> { y -> b(x)(y) }}}

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
APPEND = Z({ f -> { l -> { m ->
    IF(IS_NIL(l))(
        IF(IS_NIL(m))(
            NIL
        )(
            { x -> CONS(HEAD(m))(f(l)(TAIL(m)))(x) }
        )
    )(
        { x -> CONS(HEAD(l))(f(TAIL(l))(m))(x) }
    )
}}})
FLAT_MAP = { f -> FOLDL(NIL)({ l -> { x -> APPEND(l)(f(x)) }}) }
FOLDL = Z({ f -> { z -> { g -> { l ->
    IF(IS_NIL(l))(
        z
    )(
        { x -> f(g(z)(HEAD(l)))(g)(TAIL(l))(x) }
    )
}}}})


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

assert(toInt(SUCC(THREE))     == 4)
assert(toInt(ADD(TWO)(THREE)) == 5)
assert(toInt(PRED(THREE))     == 2)
assert(toInt(SUB(THREE)(TWO)) == 1)
assert(toInt(MUL(TWO)(THREE)) == 6)

assert(toBoolean(IS_ZERO(ZERO)))
assert(toBoolean(IS_ZERO(ONE))   == false)
assert(toBoolean(IS_ZERO(TWO))   == false)
assert(toBoolean(IS_ZERO(THREE)) == false)

//真偽値
assert(toBoolean(TRUE))
assert(toBoolean(FALSE) == false)
assert(toInt(IF(TRUE)(ONE)(TWO))  == 1)
assert(toInt(IF(FALSE)(ONE)(TWO)) == 2)

//ペア
def p1 = PAIR(ONE)(TWO)
assert(toInt(LEFT(p1))  == 1)
assert(toInt(RIGHT(p1)) == 2)

//リスト
def l1 = CONS(ONE)(CONS(TWO)(CONS(THREE)(NIL)))
def l2 = CONS(FOUR)(CONS(FIVE)(NIL))
assert(toList(l1, { toInt(it) }) == [1, 2, 3 ])
assert(toList(APPEND(l1)(l2), { toInt(it) }) == [1, 2, 3, 4, 5 ])
assert(toList(FLAT_MAP({ x -> CONS(x)(CONS(x)(NIL)) })(l1), { toInt(it) }) == [1, 1, 2, 2, 3, 3 ])
assert(toInt(FOLDL(ZERO)(ADD)(l1)) == 6)


//assert全部通ったら喜んどく
println '･:*+.\\(( °ω° ))/.:+'
