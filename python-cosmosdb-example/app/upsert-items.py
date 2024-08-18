import asyncio

from azure.core import MatchConditions
from azure.cosmos.exceptions import CosmosAccessConditionFailedError

import app.core as core


async def main():
    users, client = core.get_users_container()

    alice = await users.read_item(item="1", partition_key="JP")

    print("# upsert_item")
    result = await users.upsert_item(
        body={"id": "1", "location": "JP", "name": "Alice", "age": 19},
        etag=alice["_etag"],
        match_condition=MatchConditions.IfNotModified,
    )
    print(result)
    print()  # 空行

    print("# upsert_item（etagによる楽観排他に失敗する）")
    try:
        await users.upsert_item(
            body={"id": "1", "location": "JP", "name": "Alice", "age": 18},
            etag=alice["_etag"],
            match_condition=MatchConditions.IfNotModified,
        )
    except CosmosAccessConditionFailedError as e:
        print(e.reason)
        print()  # 空行

    print("# upsert_item（ドキュメントが存在しない場合は新規作成）")
    result = await users.upsert_item(
        body={"id": "4", "location": "US", "name": "Carol", "age": 25},
        etag="00000000-0000-0000-0000-000000000000",
        match_condition=MatchConditions.IfNotModified,
    )
    print(result)
    print()  # 空行

    await client.close()


asyncio.run(main())
