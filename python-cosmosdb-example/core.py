import os
from typing import Tuple

import urllib3
from azure.cosmos.aio import ContainerProxy, CosmosClient
from dotenv import load_dotenv

load_dotenv()


def get_cosmos_client() -> CosmosClient:
    cosmos_endpoint = os.environ["COSMOS_ENDPOINT"]
    # エミュレーター使ってる場合は証明書まわりが面倒なので検証しない
    connection_verify = not "localhost" in cosmos_endpoint
    if not connection_verify:
        urllib3.disable_warnings()

    client = CosmosClient(
        url=cosmos_endpoint,
        credential=os.environ["COSMOS_KEY"],
        connection_verify=connection_verify,
    )
    return client


def get_users_container() -> Tuple[ContainerProxy, CosmosClient]:
    client = get_cosmos_client()
    return client.get_database_client("mydb").get_container_client("users"), client
