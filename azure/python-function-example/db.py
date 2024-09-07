import argparse
import hashlib
import json
import os
from enum import Enum

import urllib3
from azure.cosmos import CosmosClient, PartitionKey

os.environ["REQUESTS_CA_BUNDLE"] = "./emulatorcert.crt"

urllib3.disable_warnings()

with open("local.settings.json", encoding="utf-8") as f:
    settings = json.load(f)

connection_string = settings["Values"]["CosmosDBConnectionString"]
client = CosmosClient.from_connection_string(connection_string)
db = client.create_database_if_not_exists(id="example_db")
container = db.create_container_if_not_exists(
    id="example_container", partition_key=PartitionKey(path="/name")
)


class Action(Enum):
    ADD = "add"
    MOD = "mod"
    DEL = "del"


parser = argparse.ArgumentParser()
parser.add_argument("--action", "-a", type=Action, default=Action.ADD)
parser.add_argument("--names", "-n", type=str, nargs="+")
args = parser.parse_args()


def make_id(name: str) -> str:
    return hashlib.md5(name.encode()).hexdigest()


match args.action:
    case Action.ADD:
        for name in args.names:
            container.create_item(
                {
                    "id": make_id(name),
                    "name": name,
                    "count": 0,
                }
            )
    case Action.MOD:
        for name in args.names:
            item = container.read_item(
                item=make_id(name),
                partition_key=name,
            )
            item["count"] += 1
            container.upsert_item(item)
    case Action.DEL:
        for name in args.names:
            item = container.read_item(
                item=make_id(name),
                partition_key=name,
            )
            container.delete_item(item, partition_key=name)
