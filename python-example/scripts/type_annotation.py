from typing import Callable, List


def hello(name: str) -> str:
    return f"Hello, {name}!"


greeting: str = hello("world")

hello_func: Callable[[str], str] = hello

sum: Callable[[int, int], int] = lambda a, b: a + b


def list(xs: List[str]):
    pass


# PEP 695 generics are not yet supported と言われてしまう。残念
#
# T = TypeVar("T")
#
# def f[T](xs: List[T]):
#     pass
#
# U = TypeVar("U", bound=Number)
#
# def g[U](xs: List[U]):
#     pass
