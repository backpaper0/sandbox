import asyncio
import time


async def say(queue: asyncio.Queue, n: int) -> None:
    await asyncio.sleep(n)
    msg = f"{time.strftime('%X')} task#{n}"
    await queue.put(msg)


async def main() -> None:
    print(f"{time.strftime('%X')} start")

    queue: asyncio.Queue = asyncio.Queue(maxsize=100)

    tasks = [asyncio.create_task(say(queue, i)) for i in range(1, 11)]

    for i in range(1, 5):
        await asyncio.sleep(3)
        print(f"{time.strftime('%X')} group#{i}")
        try:
            while True:
                msg = queue.get_nowait()
                print(f"  {msg}")
        except asyncio.QueueEmpty:
            pass

    await asyncio.gather(*tasks)
    print(f"{time.strftime('%X')} end")


asyncio.run(main())
