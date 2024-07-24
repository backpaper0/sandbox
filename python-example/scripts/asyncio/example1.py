import asyncio
import time


async def main() -> None:
    print(f"{time.strftime('%X')} hello")
    await asyncio.sleep(1)
    print(f"{time.strftime('%X')} world")


asyncio.run(main())
