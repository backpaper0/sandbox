from azure.cosmos.errors import CosmosResourceNotFoundError

import core

users = core.get_users_container()

print("# 全件取得")
items = users.read_all_items()
for item in items:
    print(item)
print()  # 空行

print("# 1件取得（idで取得）")
item = users.read_item(item="1", partition_key="JP")
print(item)
print()  # 空行

print("# 1件取得（ドキュメントが存在しない場合はエラー）")
try:
    users.read_item(item="9999", partition_key="JP")
except CosmosResourceNotFoundError as e:
    print(e.reason)
print()  # 空行

print("# 属性で取得")
items = users.query_items(
    query="select * from c where c.name = @name",
    parameters=[{"name": "@name", "value": "Bob"}],
    enable_cross_partition_query=True,
)
for item in items:
    print(item)
print()  # 空行
