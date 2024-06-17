import asyncio

from azure.cosmos.errors import CosmosResourceNotFoundError

import core


async def main():
    users, client = core.get_users_container()

    print("# 全件取得")
    items = users.read_all_items()
    async for iter in items.by_page():
        async for item in iter:
            print(item)
    print()  # 空行

    print("# 1件取得（idで取得）")
    item = await users.read_item(item="1", partition_key="JP")
    print(item)
    print()  # 空行

    print("# 1件取得（ドキュメントが存在しない場合はエラー）")
    try:
        await users.read_item(item="9999", partition_key="JP")
    except CosmosResourceNotFoundError as e:
        print(e.reason)
    print()  # 空行

    print("# 属性で取得")
    items = users.query_items(
        query="select * from c where c.name = @name",
        parameters=[{"name": "@name", "value": "Bob"}],
    )
    async for iter in items.by_page():
        async for item in iter:
            print(item)
    print()  # 空行

    await client.close()


asyncio.run(main())
