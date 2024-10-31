import asyncio

from azure.cosmos.errors import CosmosResourceNotFoundError

import app.core as core
from app.core import *


async def main():
    async with core.get_users_container() as container:
        print("# 全件取得")
        items = container.read_all_items()
        async for item in items:
            user = User(**item)
            print(user)
        print()  # 空行

        print("# 1件取得（idで取得）")
        item = await container.read_item(item="1", partition_key="JP")
        user = User(**item)
        print(user)
        print()  # 空行

        print("# 1件取得（ドキュメントが存在しない場合はエラー）")
        try:
            await container.read_item(item="9999", partition_key="JP")
        except CosmosResourceNotFoundError as e:
            print(e.reason)
        print()  # 空行

        print("# 属性で取得")
        items = container.query_items(
            query="select * from c where c.name = @name",
            parameters=[{"name": "@name", "value": "Bob"}],
        )
        async for user in wrap_users(items):
            print(user)
        print()  # 空行


asyncio.run(main())
