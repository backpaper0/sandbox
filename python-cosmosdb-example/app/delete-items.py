import asyncio

import app.core as core


async def main():
    users, client = core.get_users_container()

    items = users.read_all_items()
    async for iter in items.by_page():
        async for item in iter:
            await users.delete_item(item=item, partition_key=item["location"])

    await client.close()


asyncio.run(main())
