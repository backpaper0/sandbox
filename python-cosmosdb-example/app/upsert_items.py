import asyncio

from azure.core import MatchConditions
from azure.cosmos.exceptions import CosmosAccessConditionFailedError

import app.core as core
from app.core import User, read_user


async def main():
    async with core.get_users_container() as container:
        alice = await read_user(container=container, id="1", location="JP")

        print("# upsert_item")
        result = await container.upsert_item(
            body=User(id="1", location="JP", name="Alice", age=19).model_dump(),
            etag=alice.etag,
            match_condition=MatchConditions.IfNotModified,
        )
        print(User(**result))
        print()  # 空行

        print("# upsert_item（etagによる楽観排他に失敗する）")
        try:
            await container.upsert_item(
                body=User(id="1", location="JP", name="Alice", age=18).model_dump(),
                etag=alice.etag,
                match_condition=MatchConditions.IfNotModified,
            )
        except CosmosAccessConditionFailedError as e:
            print(e.reason)
            print()  # 空行

        print("# upsert_item（ドキュメントが存在しない場合は新規作成）")
        result = await container.upsert_item(
            body=User(id="4", location="US", name="Carol", age=25).model_dump(),
            etag="00000000-0000-0000-0000-000000000000",
            match_condition=MatchConditions.IfNotModified,
        )
        print(User(**result))
        print()  # 空行


asyncio.run(main())
