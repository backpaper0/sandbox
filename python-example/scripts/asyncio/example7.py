import asyncio
import time


async def say(event: asyncio.Event, n: int) -> None:
    print(f"{time.strftime('%X')} ready... #{n}")
    await event.wait()
    print(f"{time.strftime('%X')} go! #{n}")


async def main() -> None:
    print(f"{time.strftime('%X')} start")
    event = asyncio.Event()
    tasks = [asyncio.create_task(say(event, i)) for i in range(1, 4)]
    await asyncio.sleep(3)
    event.set()
    await asyncio.gather(*tasks)
    print(f"{time.strftime('%X')} end")


asyncio.run(main())
