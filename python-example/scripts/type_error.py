def foo(a: str) -> str:
    if a < 0:
        return 0
    return a + 0


a: int = foo("FOO")
b: str = foo(0)
c: str = foo("FOO")

d = "bar"
e = d + 0
