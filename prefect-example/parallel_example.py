from prefect import flow, task
import time


@task
def hello() -> None:
    time.sleep(1)
    print("Hello")
    time.sleep(1)


@task
def world() -> None:
    time.sleep(1)
    print("World")
    time.sleep(1)


@task
def sleep(seconds: int) -> None:
    time.sleep(seconds)


@flow
def parallel_example() -> None:
    h = hello.submit()
    w = world.submit()
    s = sleep.map([1, 2, 3], wait_for=[h, w])
    s.wait()


if __name__ == "__main__":
    parallel_example()
