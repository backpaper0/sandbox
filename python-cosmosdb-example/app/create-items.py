import asyncio

from azure.cosmos.exceptions import CosmosResourceExistsError

import app.core as core


async def main():
    async with core.get_users_container() as users:
        items = [
            {"id": "1", "location": "JP", "name": "Alice", "age": 20},
            {"id": "2", "location": "US", "name": "Bob", "age": 21},
            {"id": "3", "location": "US", "name": "Eve", "age": 22},
        ]
        for item in items:
            try:
                result = await users.create_item(item)

                print("# Item created")
                print(result)
                print()  # 空行

            except CosmosResourceExistsError as e:
                print("# Error occurred")
                print(e)
                print()  # 空行


asyncio.run(main())
