from azure.core import MatchConditions
from azure.cosmos.exceptions import CosmosAccessConditionFailedError

import core

client = core.get_cosmos_client()

db = client.get_database_client("mydb")

users = db.get_container_client("users")


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
