"""
抽象構文木

- 値（整数、真偽、文字列）
- バイナリ演算（四則演算、比較演算、論理演算、文字列結合演算）
- 変数代入
- 変数参照
- 分岐
- 繰り返し
- 関数定義
- 関数適用
"""
from typing import Any, Literal

class Expr:
    pass

Type = Literal["Integer", "Boolean", "String"]

class Val(Expr):
    def __init__(self, type: Type, value: Any):
        self.type = type
        self.value = value

Operator = Literal["+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "&&", "||"]

class BinOp(Expr):
    def __init__(self, op: Operator, lhs: Expr, rhs: Expr):
        self.op = op
        self.lhs = lhs
        self.rhs = rhs

class Assign(Expr):
    def __init__(self, name: str, value: Expr):
        self.name = name
        self.value = value

class Var(Expr):
    def __init__(self, name: str):
        self.name = name

class If(Expr):
    def __init__(self, cond: Expr, then: Expr, els: Expr):
        self.cond = cond
        self.then = then
        self.els = els

class While(Expr):
    def __init__(self, cond: Expr, body: Expr):
        self.cond = cond
        self.body = body

class FunDef(Expr):
    def __init__(self, name: str, params: list[str], body: Expr):
        self.name = name
        self.params = params
        self.body = body

class FunApp(Expr):
    def __init__(self, name: str, args: list[Expr]):
        self.name = name
        self.args = args

class Seq(Expr):
    def __init__(self, exprs: list[Expr]):
        self.exprs = exprs
