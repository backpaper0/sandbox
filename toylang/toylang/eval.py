"""
評価器

動作確認
poetry run python -m toylang.eval
"""

from .ast import *
from typing import Any

def eval(expr: Expr) -> Any:
    return do_eval(expr, {})

def do_eval(expr: Expr, env: dict[str, Any]) -> Any:
    if isinstance(expr, Val):
        return expr.value
    elif isinstance(expr, BinOp):
        lhs = do_eval(expr.lhs, env)
        rhs = do_eval(expr.rhs, env)
        match expr.op:
            case "+":
                return lhs + rhs
            case "-":
                return lhs - rhs
            case "*":
                return lhs * rhs
            case "/":
                return int(lhs / rhs)
            case "<":
                return lhs < rhs
            case "<=":
                return lhs <= rhs
            case ">":
                return lhs > rhs
            case ">=":
                return lhs >= rhs
            case "==":
                return lhs == rhs
            case "!=":
                return lhs != rhs
            case "&&":
                return lhs and rhs
            case "||":
                return lhs or rhs


if __name__ == "__main__":
    expr1 = BinOp("+", Val("Integer", 1), Val("Integer", 2))
    ret1 = eval(expr1)
    print(f"1 + 2 => {ret1}") # 3

    expr2 = BinOp(
        "/",
        BinOp(
            "*",
            BinOp(
                "+",
                BinOp(
                    "+",
                    Val("Integer", 1),
                    Val("Integer", 2),
                ),
                Val("Integer", 8),
            ),
            BinOp(
                "-",
                Val("Integer", 63),
                Val("Integer", 54),
            ),
        ),
        Val("Integer", 9),
    )
    ret2 = eval(expr2)
    print(f"(1 + 2 + 8) * (63 - 54) / 9 => {ret2}") # 11
