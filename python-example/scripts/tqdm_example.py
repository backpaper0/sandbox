import asyncio
from typing import Iterator

from tqdm import tqdm, trange
from tqdm.asyncio import tqdm as atqdm


async def f(i: int) -> None:
    await asyncio.sleep(0.01 * i)


def g(size: int) -> Iterator[int]:
    for i in range(0, size):
        yield i


async def main() -> None:
    size = 100

    # Shortcut for tqdm(range(args), *kwargs).
    for _ in trange(0, size, desc="シンプルな例"):
        await asyncio.sleep(0.01)

    # イテレーターは合計数がわからないからプログレスバーが表示されない
    for _ in tqdm(g(size), desc="イテレーター"):
        await asyncio.sleep(0.01)

    # 合計数がわかっている場合はtotalを設定すれば良い
    for _ in tqdm(g(size), total=size, desc="イテレーター w/ 合計数"):
        await asyncio.sleep(0.01)

    with tqdm(total=size, desc="明示的にupdate") as progress_bar:
        for _ in range(0, size):
            await asyncio.sleep(0.01)
            progress_bar.update()

    coros = [f(i) for i in range(0, size)]
    tasks = [asyncio.create_task(coro) for coro in coros]
    await atqdm.gather(*tasks, desc="非同期IO")


if __name__ == "__main__":
    asyncio.run(main())
