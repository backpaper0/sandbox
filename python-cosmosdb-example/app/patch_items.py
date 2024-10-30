import asyncio

from azure.core import MatchConditions
from azure.cosmos.exceptions import (
    CosmosAccessConditionFailedError,
    CosmosResourceNotFoundError,
)

import app.core as core
from app.core import User, read_user


async def main():
    async with core.get_users_container() as container:
        alice = await read_user(container=container, id="1", location="JP")
        patch_operations = [
            {"op": "incr", "path": "/age", "value": 1},
        ]

        print("# patch_item")
        result = await container.patch_item(
            item="1",
            partition_key="JP",
            patch_operations=patch_operations,
            etag=alice.etag,
            match_condition=MatchConditions.IfNotModified,
        )
        print(User(**result))
        print()  # 空行

        print("# patch_item（etagによる楽観排他に失敗する）")
        try:
            await container.patch_item(
                item="1",
                partition_key="JP",
                patch_operations=patch_operations,
                etag=alice.etag,
                match_condition=MatchConditions.IfNotModified,
            )
        except CosmosAccessConditionFailedError as e:
            print(e.reason)
            print()  # 空行

        print("# patch_item（ドキュメントが存在しない場合はエラー）")
        try:
            await container.patch_item(
                item="5",
                partition_key="JP",
                patch_operations=patch_operations,
                etag="00000000-0000-0000-0000-000000000000",
                match_condition=MatchConditions.IfNotModified,
            )
        except CosmosResourceNotFoundError as e:
            print(e.reason)
            print()  # 空行


asyncio.run(main())
