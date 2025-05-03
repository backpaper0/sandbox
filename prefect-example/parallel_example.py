from prefect import flow, task
import time
import threading
import os


def _f(name: str) -> None:
    print(f"PID:{os.getpid()} ThreadID:{threading.get_ident()} name:{name}")


@task
def hello() -> None:
    _f("hello")
    time.sleep(1)
    print("Hello")
    time.sleep(1)


@task
def world() -> None:
    _f("world")
    time.sleep(1)
    print("World")
    time.sleep(1)


@task
def sleep(seconds: int) -> None:
    _f("sleep")
    time.sleep(seconds)


@flow
def parallel_example() -> None:
    _f("parallel_example")
    h = hello.submit()
    w = world.submit()
    s = sleep.map([1, 2, 3], wait_for=[h, w])
    s.wait()


if __name__ == "__main__":
    _f("main")
    parallel_example()
