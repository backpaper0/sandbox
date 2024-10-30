import asyncio

from azure.cosmos.errors import CosmosResourceNotFoundError

import app.core as core


async def main():
    async with core.get_users_container() as users:
        print("# 全件取得")
        items = users.read_all_items()
        async for item in items:
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
        async for item in items:
            print(item)
        print()  # 空行


asyncio.run(main())
