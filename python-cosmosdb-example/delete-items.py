import core

users = core.get_users_container()

items = users.query_items(query="select * from c", enable_cross_partition_query=True)
for item in items:
    users.delete_item(item, item["location"])
