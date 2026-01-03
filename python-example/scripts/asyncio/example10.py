import asyncio
import time


size = 5


def blocking_io(a: int, b: int) -> int:
    time.sleep(1)
    return a * b


async def async_await_wrapper(a: int, b: int) -> int:
    loop = asyncio.get_running_loop()
    return await loop.run_in_executor(None, blocking_io, a, b)


def run_blocking_io() -> None:
    start = time.time()
    results = [blocking_io(i, i + 1) for i in range(size)]
    end = time.time()
    elapsed = end - start
    print(f"[Blocking IO] {', '.join([str(r) for r in results])} ({elapsed:.2f} sec)")


async def run_async_await_wrapper() -> None:
    start = time.time()
    coros = [async_await_wrapper(i, i + 1) for i in range(size)]
    results = await asyncio.gather(*coros)
    end = time.time()
    elapsed = end - start
    print(f"[async/await] {', '.join([str(r) for r in results])} ({elapsed:.2f} sec)")


async def main() -> None:
    run_blocking_io()
    await run_async_await_wrapper()


if __name__ == "__main__":
    asyncio.run(main())
