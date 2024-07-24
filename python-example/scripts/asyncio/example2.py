import asyncio
import time


async def say(sleep: float, msg: str) -> None:
    await asyncio.sleep(sleep)
    print(f"{time.strftime('%X')} {msg}")


async def main() -> None:
    print(f"{time.strftime('%X')} start")
    said1 = say(2, "hello")
    said2 = say(1, "world")
    await said1
    await said2
    print(f"{time.strftime('%X')} end")


asyncio.run(main())
