import asyncio

import app.core as core
from app.core import wrap_users


async def main():
    async with core.get_users_container() as container:
        items = container.read_all_items()
        async for user in wrap_users(items):
            await container.delete_item(item=user.id, partition_key=user.location)


asyncio.run(main())
