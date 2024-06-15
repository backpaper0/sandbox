from azure.cosmos.exceptions import CosmosResourceExistsError

import core

users = core.get_users_container()

items = [
    {"id": "1", "location": "JP", "name": "Alice", "age": 20},
    {"id": "2", "location": "US", "name": "Bob", "age": 21},
    {"id": "3", "location": "US", "name": "Eve", "age": 22},
]
for item in items:
    try:
        result = users.create_item(item)

        print("# Item created")
        print(result)
        print()  # 空行

    except CosmosResourceExistsError as e:
        print("# Error occurred")
        print(e)
        print()  # 空行
