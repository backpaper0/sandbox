import weave
from dotenv import load_dotenv

load_dotenv()


@weave.op()
def hello(name: str) -> str:
    return f"Hello, {name}!"


weave.init("op_example")

print(hello("world"))
print(hello("foobar"))
