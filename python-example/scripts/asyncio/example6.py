import asyncio
import time


async def say(semaphore: asyncio.Semaphore, msg: str) -> None:
    async with semaphore:
        await asyncio.sleep(1)
        print(f"{time.strftime('%X')} {msg}")


async def main() -> None:
    print(f"{time.strftime('%X')} start")
    semaphore = asyncio.Semaphore(3)
    tasks = [say(semaphore, f"msg#{i}") for i in range(1, 10)]
    await asyncio.gather(*tasks)
    print(f"{time.strftime('%X')} end")


asyncio.run(main())
