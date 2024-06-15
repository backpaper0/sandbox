import core

client = core.get_cosmos_client()

db = client.get_database_client("mydb")

users = db.get_container_client("users")

items = users.query_items(query="select * from c", enable_cross_partition_query=True)
for item in items:
    users.delete_item(item, item["location"])
