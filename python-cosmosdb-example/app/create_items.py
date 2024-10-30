import asyncio

from azure.cosmos.exceptions import CosmosResourceExistsError

import app.core as core
from app.core import User


async def main():
    async with core.get_users_container() as container:
        users = [
            User(id="1", location="JP", name="Alice", age=20),
            User(id="2", location="US", name="Bob", age=21),
            User(id="3", location="US", name="Eve", age=22),
        ]
        for user in users:
            try:
                item = user.model_dump()
                result = await container.create_item(item)
                created_user = User(**result)

                print("# Item created")
                print(created_user)
                print()  # 空行

            except CosmosResourceExistsError as e:
                print("# Error occurred")
                print(e)
                print()  # 空行


asyncio.run(main())
