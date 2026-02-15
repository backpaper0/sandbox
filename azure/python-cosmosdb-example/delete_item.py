import argparse
import asyncio

from azure.cosmos.aio import CosmosClient
from azure.identity.aio import DefaultAzureCredential

from settings import Settings


async def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("id", help="削除するアイテムのID")
    parser.add_argument("category", help="パーティションキー（category）")
    args = parser.parse_args()

    settings = Settings()  # type: ignore[call-arg]
    credential = settings.cosmos_key or DefaultAzureCredential()
    async with CosmosClient(settings.cosmos_endpoint, credential=credential) as client:
        database = client.get_database_client(settings.cosmos_database_name)
        container = database.get_container_client(settings.cosmos_container_name)
        await container.delete_item(item=args.id, partition_key=args.category)
        print(f"Deleted: {args.id}")


if __name__ == "__main__":
    asyncio.run(main())
