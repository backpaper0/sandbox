# 型なしラムダ計算で遊ぼう
#
# fizzbuzzをやるために必要な要素
# - [x] 数値(チャーチ数)
# - [x] インクリメント(後者関数)
# - [ ] 比較演算
# - [x] 真偽値
# - [x] タプル
# - [x] リスト
# - [ ] 範囲
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

# ループ

# 条件分岐
IF = lambda c: lambda t: lambda e: c(t)(e)

# 文字

# 型変換処理
def to_num(n):
    return n(lambda x: x + 1)(0)

def to_bool(n):
    return n(True)(False)

def to_num_list(n):
    if to_bool(IS_NIL(n)):
        return []
    return [to_num(HEAD(n)), to_num_list(TAIL(n))]

# print(to_num(_0))
# print(to_num(_1))
# print(to_num(_2))
# print(to_num(_3))
# print(to_num(SUCC(_3)))

# print(to_bool(TRUE))
# print(to_bool(FALSE))
# print(to_num(IF(TRUE)(_1)(_0)))
# print(to_num(IF(FALSE)(_1)(_0)))

# print(to_num(LEFT(TUPLE(_1)(_2))))
# print(to_num(RIGHT(TUPLE(_1)(_2))))

lst = CONS(_1)(
    CONS(_2)(
        CONS(_3)(
            NIL
        )
    )
)

print(to_num_list(lst))
