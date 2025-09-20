import asyncio

from azure.cosmos import PartitionKey

import app.core as core


async def main():
    async with core.get_cosmos_client() as client:
        database = await client.create_database_if_not_exists(
            id="mydb",
            offer_throughput=400,
        )
        print(await database.read())

        users = await database.create_container_if_not_exists(
            id="users",
            partition_key=PartitionKey(
                path="/location",
            ),
        )
        print(await users.read())


asyncio.run(main())
