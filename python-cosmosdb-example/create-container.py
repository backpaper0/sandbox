from azure.cosmos import PartitionKey

import core

client = core.get_cosmos_client()

database = client.create_database_if_not_exists(
    id="mydb",
    offer_throughput=400,
)

container = database.create_container_if_not_exists(
    id="users",
    partition_key=PartitionKey(
        path="/location",
    ),
)
