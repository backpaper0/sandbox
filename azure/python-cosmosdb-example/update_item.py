import argparse
import asyncio
import json

from azure.cosmos.aio import CosmosClient
from azure.identity.aio import DefaultAzureCredential

from settings import Settings


async def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("id", help="更新するアイテムのID")
    parser.add_argument("category", help="パーティションキー（category）")
    parser.add_argument(
        "patch",
        help='パッチ操作（JSON文字列、例: \'[{"op": "incr", "path": "/quantity", "value": 1}]\'）',
    )
    args = parser.parse_args()
    operations = json.loads(args.patch)

    settings = Settings()  # type: ignore[call-arg]
    credential = settings.cosmos_key or DefaultAzureCredential()
    async with CosmosClient(settings.cosmos_endpoint, credential=credential) as client:
        database = client.get_database_client(settings.cosmos_database_name)
        container = database.get_container_client(settings.cosmos_container_name)
        result = await container.patch_item(
            item=args.id,
            partition_key=args.category,
            patch_operations=operations,
        )
        print(
            f"Updated: {result['id']} ({result['name']}, quantity={result['quantity']})"
        )


if __name__ == "__main__":
    asyncio.run(main())
