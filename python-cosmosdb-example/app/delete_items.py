import asyncio

import app.core as core


async def main():
    async with core.get_users_container() as users:
        items = users.read_all_items()
        async for item in items:
            await users.delete_item(item=item, partition_key=item["location"])


asyncio.run(main())
