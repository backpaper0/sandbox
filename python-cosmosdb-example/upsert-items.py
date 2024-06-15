from azure.core import MatchConditions
from azure.cosmos.exceptions import CosmosAccessConditionFailedError

import core

users = core.get_users_container()


alice = users.read_item(item="1", partition_key="JP")

print("# upsert_item")
result = users.upsert_item(
    body={"id": "1", "location": "JP", "name": "Alice", "age": 19},
    etag=alice["_etag"],
    match_condition=MatchConditions.IfNotModified,
)
print(result)
print()  # 空行

print("# upsert_item（etagによる楽観排他に失敗する）")
try:
    users.upsert_item(
        body={"id": "1", "location": "JP", "name": "Alice", "age": 18},
        etag=alice["_etag"],
        match_condition=MatchConditions.IfNotModified,
    )
except CosmosAccessConditionFailedError as e:
    print(e.reason)
    print()  # 空行
