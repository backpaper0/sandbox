"""楽観排他制御（Optimistic Concurrency Control）を使用したアイテム更新の例。

Cosmos DB の ETag を利用し、読み取り時点から変更されていない場合のみ更新を適用する。
他のクライアントが先に更新していた場合は 412 Precondition Failed が返る。
"""

import argparse
import asyncio
import json

from azure.cosmos.aio import CosmosClient
from azure.cosmos.exceptions import CosmosAccessConditionFailedError
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
    parser.add_argument(
        "--simulate-conflict",
        action="store_true",
        help="読み取りと更新の間に別の書き込みを挟み、コンフリクトを発生させる",
    )
    args = parser.parse_args()
    operations = json.loads(args.patch)

    settings = Settings()  # type: ignore[call-arg]
    credential = settings.cosmos_key or DefaultAzureCredential()
    async with CosmosClient(settings.cosmos_endpoint, credential=credential) as client:
        database = client.get_database_client(settings.cosmos_database_name)
        container = database.get_container_client(settings.cosmos_container_name)

        # 1. アイテムを読み取り、現在の ETag を取得する
        item = await container.read_item(item=args.id, partition_key=args.category)
        etag = item["_etag"]
        print(f"Read:    {item['id']} (quantity={item['quantity']}, etag={etag})")

        # --simulate-conflict: 別のクライアントによる更新をシミュレートする
        if args.simulate_conflict:
            rival = await container.patch_item(
                item=args.id,
                partition_key=args.category,
                patch_operations=[{"op": "incr", "path": "/quantity", "value": -1}],
            )
            print(
                f"Rival:   {rival['id']} (quantity={rival['quantity']}, etag={rival['_etag']})"
            )

        # 2. ETag を if_match に指定して更新する
        #    読み取り後に他のクライアントが同じアイテムを変更していた場合、
        #    ETag が一致しないため CosmosAccessConditionFailedError (HTTP 412) が発生する。
        try:
            result = await container.patch_item(
                item=args.id,
                partition_key=args.category,
                patch_operations=operations,
                if_match=etag,
            )
            print(
                f"Updated: {result['id']} (quantity={result['quantity']}, etag={result['_etag']})"
            )
        except CosmosAccessConditionFailedError:
            print(
                "Conflict: アイテムは読み取り後に別のクライアントによって変更されています。"
                "再読み込みしてリトライしてください。"
            )


if __name__ == "__main__":
    asyncio.run(main())
