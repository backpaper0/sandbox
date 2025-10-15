import asyncio
from typing import Any

from prefect import flow, task
from prefect.futures import PrefectFuture


@task
async def a() -> None:
    await asyncio.sleep(3)


@task
async def b1() -> None:
    await asyncio.sleep(1)


@task
async def b2() -> None:
    await asyncio.sleep(2)


@task
async def c1() -> None:
    await asyncio.sleep(3)


@task
async def c2() -> None:
    await asyncio.sleep(4)


@task
async def d() -> None:
    await asyncio.sleep(5)


@task
async def e() -> None:
    await asyncio.sleep(1)


@task
async def f() -> None:
    await asyncio.sleep(3)


@task
async def g() -> None:
    await asyncio.sleep(3)


@flow
def complex_parallel_flow() -> None:
    future_a: PrefectFuture[Any] = a.submit()
    future_b1: PrefectFuture[Any] = b1.submit(return_state=False, wait_for=[future_a])
    future_b2: PrefectFuture[Any] = b2.submit(return_state=False, wait_for=[future_a])
    future_c1: PrefectFuture[Any] = c1.submit(return_state=False, wait_for=[future_a])
    future_c2: PrefectFuture[Any] = c2.submit(return_state=False, wait_for=[future_a])
    future_d: PrefectFuture[Any] = d.submit(
        return_state=False, wait_for=[future_b1, future_b2]
    )
    future_e: PrefectFuture[Any] = e.submit(
        return_state=False, wait_for=[future_c1, future_c2]
    )
    future_f: PrefectFuture[Any] = f.submit(
        return_state=False, wait_for=[future_d, future_e]
    )
    future_g: PrefectFuture[Any] = g.submit(return_state=False, wait_for=[future_f])
    future_g.wait()


if __name__ == "__main__":
    complex_parallel_flow()
