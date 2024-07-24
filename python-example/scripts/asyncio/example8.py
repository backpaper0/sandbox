import asyncio
import time


async def say(barrier: asyncio.Barrier, n: int) -> None:
    print(f"{time.strftime('%X')} ready... #{n} barrier={barrier}")
    await barrier.wait()
    print(f"{time.strftime('%X')} go! #{n} barrier={barrier}")


async def main() -> None:
    print(f"{time.strftime('%X')} start")
    barrier = asyncio.Barrier(3)
    tasks = []
    tasks.append(asyncio.create_task(say(barrier, 1)))
    await asyncio.sleep(1)
    tasks.append(asyncio.create_task(say(barrier, 2)))
    await asyncio.sleep(1)
    tasks.append(asyncio.create_task(say(barrier, 3)))
    await asyncio.gather(*tasks)
    print(f"{time.strftime('%X')} end")


asyncio.run(main())
