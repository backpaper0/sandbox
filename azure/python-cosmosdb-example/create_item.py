import argparse
import asyncio
import json

from azure.cosmos import PartitionKey
from azure.cosmos.aio import CosmosClient
from azure.identity.aio import DefaultAzureCredential

from settings import Settings


async def main():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "item",
        nargs="?",
        default='{"id": "1", "category": "gear", "name": "Helmet", "quantity": 5}',
        help="作成するアイテム（JSON文字列）",
    )
    args = parser.parse_args()
    item = json.loads(args.item)

    settings = Settings()  # type: ignore[call-arg]
    credential = settings.cosmos_key or DefaultAzureCredential()
    async with CosmosClient(settings.cosmos_endpoint, credential=credential) as client:
        database = await client.create_database_if_not_exists(
            settings.cosmos_database_name
        )
        container = await database.create_container_if_not_exists(  # type: ignore[union-attr]
            id=settings.cosmos_container_name,
            partition_key=PartitionKey(path="/category"),
        )
        result = await container.create_item(body=item)  # type: ignore[union-attr]
        print(f"Created: {result['id']} ({result['name']})")


if __name__ == "__main__":
    asyncio.run(main())
