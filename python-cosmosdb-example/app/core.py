from typing import Tuple

import urllib3
from azure.cosmos.aio import ContainerProxy, CosmosClient
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )
    cosmos_endpoint: str = ""
    cosmos_key: str = ""


settings = Settings()


def get_cosmos_client() -> CosmosClient:
    # エミュレーター使ってる場合は証明書まわりが面倒なので検証しない
    connection_verify = not "localhost" in settings.cosmos_endpoint
    if not connection_verify:
        urllib3.disable_warnings()

    client = CosmosClient(
        url=settings.cosmos_endpoint,
        credential=settings.cosmos_key,
        connection_verify=connection_verify,
    )
    return client


class UsersAndClient:
    users: ContainerProxy
    client: CosmosClient

    def __init__(self, users: ContainerProxy, client: CosmosClient):
        self.users = users
        self.client = client

    async def __aenter__(self) -> ContainerProxy:
        return self.users

    async def __aexit__(self, exc_type, exc_val, exc_tb) -> None:
        await self.client.close()


def get_users_container() -> UsersAndClient:
    client = get_cosmos_client()
    users = client.get_database_client("mydb").get_container_client("users")
    return UsersAndClient(users, client)
