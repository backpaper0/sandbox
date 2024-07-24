import asyncio
import time


async def say(sleep: float, msg: str) -> str:
    await asyncio.sleep(sleep)
    return f"{time.strftime('%X')} {msg}"


async def main() -> None:
    print(f"{time.strftime('%X')} start")
    said1 = say(2, "hello")
    said2 = say(1, "world")
    msg1, msg2 = await asyncio.gather(said1, said2)
    print(f"{time.strftime('%X')} {msg1}")
    print(f"{time.strftime('%X')} {msg2}")
    print(f"{time.strftime('%X')} end")


asyncio.run(main())
