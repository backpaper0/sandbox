import asyncio
import time


async def say(sleep: float, msg: str) -> str:
    await asyncio.sleep(sleep)
    return f"{time.strftime('%X')} {msg}"


async def main() -> None:
    print(f"{time.strftime('%X')} start")

    said1 = await say(1, "hello")
    print(said1)

    said2 = await say(1, "world")
    print(said2)

    print(f"{time.strftime('%X')} end")


asyncio.run(main())
