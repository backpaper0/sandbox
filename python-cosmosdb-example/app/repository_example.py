"""
poetry run python -m app.repository_example
"""

import asyncio

from azure.cosmos.aio import ContainerProxy
from azure.cosmos.errors import CosmosResourceNotFoundError

import app.core as core
from app.core import *


class UserRepository:
    container: ContainerProxy

    def __init__(self, container: ContainerProxy):
        self.container = container

    async def get(self, id: str, location: str) -> Optional[User]:
        try:
            item = await self.container.read_item(item=id, partition_key=location)
            return User(**item)
        except CosmosResourceNotFoundError:
            return None

    async def find_all(self, location: str) -> AsyncGenerator[User, None]:
        items = self.container.query_items(
            query="SELECT * FROM c",
            partition_key=location,
        )
        async for item in items:
            yield User(**item)

    async def create_user(self, user: User) -> User:
        item = await self.container.create_item(body=user.model_dump())
        return User(**item)

    async def delete_user(self, id: str, location: str) -> None:
        await self.container.delete_item(item=id, partition_key=location)


async def main():
    async with core.get_users_container() as container:
        repos = UserRepository(container=container)

        user1 = await repos.create_user(
            User(id="4", location="US", name="Ivan", age=25)
        )
        print(f"Created user #1: {user1}")
        print()  # 空行

        user2 = await repos.create_user(
            User(id="5", location="US", name="Mallory", age=28)
        )
        print(f"Created user #2: {user2}")
        print()  # 空行

        user3 = await repos.get(id="4", location="US")
        print(f"Get user #1: {user3}")
        print()  # 空行

        user4 = await repos.get(id="5", location="US")
        print(f"Get user #2: {user4}")
        print()  # 空行

        print(f"Found user:")
        async for user in repos.find_all(location="US"):
            print(f"  {user}")

        await repos.delete_user(id="4", location="US")
        await repos.delete_user(id="5", location="US")


asyncio.run(main())
