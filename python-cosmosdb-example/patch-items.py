from azure.core import MatchConditions
from azure.cosmos.exceptions import (
    CosmosAccessConditionFailedError,
    CosmosResourceNotFoundError,
)

import core

users = core.get_users_container()


alice = users.read_item(item="1", partition_key="JP")
patch_operations = [
    {"op": "incr", "path": "/age", "value": 1},
]

print("# patch_item")
result = users.patch_item(
    item="1",
    partition_key="JP",
    patch_operations=patch_operations,
    etag=alice["_etag"],
    match_condition=MatchConditions.IfNotModified,
)
print(result)
print()  # 空行

print("# patch_item（etagによる楽観排他に失敗する）")
try:
    users.patch_item(
        item="1",
        partition_key="JP",
        patch_operations=patch_operations,
        etag=alice["_etag"],
        match_condition=MatchConditions.IfNotModified,
    )
except CosmosAccessConditionFailedError as e:
    print(e.reason)
    print()  # 空行

print("# patch_item（ドキュメントが存在しない場合はエラー）")
try:
    users.patch_item(
        item="5",
        partition_key="JP",
        patch_operations=patch_operations,
        etag="00000000-0000-0000-0000-000000000000",
        match_condition=MatchConditions.IfNotModified,
    )
except CosmosResourceNotFoundError as e:
    print(e.reason)
    print()  # 空行
