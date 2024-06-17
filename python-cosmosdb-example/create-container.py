import asyncio

from azure.cosmos import PartitionKey

import core


async def main():

    client = core.get_cosmos_client()

    database = await client.create_database_if_not_exists(
        id="mydb",
        offer_throughput=400,
    )

    container = await database.create_container_if_not_exists(
        id="users",
        partition_key=PartitionKey(
            path="/location",
        ),
    )

    await client.close()


asyncio.run(main())
