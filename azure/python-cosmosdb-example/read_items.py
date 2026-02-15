import argparse
import asyncio

from azure.cosmos.aio import CosmosClient
from azure.identity.aio import DefaultAzureCredential

from settings import Settings


async def main():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--query",
        default="SELECT * FROM c",
        help="実行するクエリ（デフォルト: 全件取得）",
    )
    args = parser.parse_args()

    settings = Settings()  # type: ignore[call-arg]
    credential = settings.cosmos_key or DefaultAzureCredential()
    async with CosmosClient(settings.cosmos_endpoint, credential=credential) as client:
        database = client.get_database_client(settings.cosmos_database_name)
        container = database.get_container_client(settings.cosmos_container_name)
        items = [item async for item in container.query_items(query=args.query)]
        if items:
            for item in items:
                print(
                    f"  id={item['id']}, name={item.get('name')}, quantity={item.get('quantity')}"
                )
        else:
            print("No items found.")


if __name__ == "__main__":
    asyncio.run(main())
