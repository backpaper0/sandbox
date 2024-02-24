# 型なしラムダ計算で遊ぼう
#
# fizzbuzzをやるために必要な要素
# - [x] 数値(チャーチ数)
# - [x] インクリメント(後者関数)
# - [ ] 比較演算
# - [x] 真偽値
# - [x] タプル
# - [x] リスト
# - [x] 範囲
# - [ ] ループ
# - [x] 条件分岐
# - [ ] 文字

# %%
# Python版の見本
def fizzbuzz():
    for n in range(1, 20):
        if n == 15:
            yield "fizzbuzz"
        elif n == 3:
            yield "fizz"
        elif n == 5:
            yield "buzz"
        else:
            yield n

print([n for n in fizzbuzz()])

# %%
# 型なしラムダ計算

# 数値(チャーチ数)
_0 = lambda f: lambda x: x
_1 = lambda f: lambda x: f(x)
_2 = lambda f: lambda x: f(f(x))
_3 = lambda f: lambda x: f(f(f(x)))

# インクリメント(後者関数)
SUCC = lambda n: lambda f: lambda x: f(n(f)(x))

# 比較演算
DECREMENT = lambda n: LEFT(n(lambda x: TUPLE(RIGHT(x))(SUCC(RIGHT(x))))(TUPLE(_0)(_0)))
SUB = lambda a: lambda b: b(DECREMENT)(a)
IS_ZERO = lambda n: n(lambda x: FALSE)(TRUE)
# LT = lambda a: lambda b: IS_ZERO(SUB(b)(a))(FALSE)(TRUE)
EQ = lambda a: lambda b: IS_ZERO(SUB(a)(b))(IS_ZERO(SUB(b)(a)))(FALSE)

# 真偽値
TRUE = lambda l: lambda r: l
FALSE = lambda l: lambda r: r

# タプル
TUPLE = lambda l: lambda r: lambda f: f(l)(r)
LEFT = lambda v: v(TRUE)
RIGHT = lambda v: v(FALSE)

# リスト
CONS = lambda h: lambda t: TUPLE(TUPLE(FALSE)(h))(t)
NIL = TUPLE(TRUE)(TRUE)
HEAD = lambda l: RIGHT(LEFT(l))
TAIL = RIGHT
IS_NIL = lambda l: LEFT(LEFT(l))

# 範囲
RANGE = lambda a: lambda b: RIGHT(SUB(b)(a)(lambda x: TUPLE(DECREMENT(LEFT(x)))(CONS(LEFT(x))(RIGHT(x))))(TUPLE(DECREMENT(b))(NIL)))

# ループ(というか射影)
# λf. (λx. f (λy. x x y)) (λx. f (λy. x x y))
Z = lambda f: (lambda x: f(lambda y: x(x)(y)))(lambda x: f(lambda y: x(x)(y)))
MAP = Z(lambda f: lambda g: lambda l: IF(IS_NIL(l))(NIL)(lambda x: CONS(g(HEAD(l)))(f(g)(TAIL(l)))(x)))

# 条件分岐
IF = lambda c: lambda t: lambda e: c(t)(e)

# 文字

# 型変換処理
def to_num(n):
    return n(lambda x: x + 1)(0)

def to_bool(n):
    return n(True)(False)

def to_num_list(n):
    print(n)
    if to_bool(IS_NIL(n)):
        return []
    return [to_num(HEAD(n)), to_num_list(TAIL(n))]

# MAPがうまくいっていない。TypeError: 'bool' object is not callable
# print(to_num_list(RANGE(_0)(_3)))
print(to_num_list(MAP(lambda x: x)(RANGE(_0)(_3))))
