import asyncio
import time


async def say(lock: asyncio.Lock, sleep: float, msg: str) -> None:
    async with lock:
        await asyncio.sleep(sleep)
        print(f"{time.strftime('%X')} {msg}")


async def main() -> None:
    print(f"{time.strftime('%X')} start")
    lock = asyncio.Lock()
    said1 = asyncio.create_task(say(lock, 2, "hello"))
    said2 = asyncio.create_task(say(lock, 1, "world"))
    await said1
    await said2
    print(f"{time.strftime('%X')} end")


asyncio.run(main())
