"""日時の扱いデモ。

Azure Cosmos DB のクライアントは内部で json.dumps() を使うため、
datetime をそのまま登録できない。Pydantic の model_validator で
datetime <-> float (エポック秒) 変換を行う例。

Usage:
    uv run demo_datetime.py
    uv run demo_datetime.py "2025-06-15T10:30:00"
"""

import argparse
import asyncio
from datetime import datetime

from azure.cosmos import PartitionKey
from azure.cosmos.aio import CosmosClient
from azure.cosmos.exceptions import CosmosResourceNotFoundError
from azure.identity.aio import DefaultAzureCredential
from pydantic import BaseModel, model_validator

from settings import Settings

CONTAINER_NAME = "demo_datetime"


class DemoDatetimeModel(BaseModel):
    """アプリケーション側のモデル（datetime型）。"""

    id: str
    datetime: datetime


class DemoDatetimeCosmosModel(BaseModel):
    """Cosmos DB 保存用モデル（float型）。"""

    id: str
    datetime: float

    @model_validator(mode="before")
    @classmethod
    def convert_datetime_to_float(cls, values: dict | BaseModel) -> dict:
        if isinstance(values, dict):
            v = values
        elif isinstance(values, BaseModel):
            v = values.model_dump()
        else:
            raise TypeError(f"Unexpected type: {type(values)}")
        if "datetime" in v and isinstance(v["datetime"], datetime):
            v["datetime"] = v["datetime"].timestamp()
        return v


async def main():
    parser = argparse.ArgumentParser(description="datetime 変換デモ")
    parser.add_argument(
        "datetime_str",
        nargs="?",
        default="2024-12-31T21:22:00",
        help="登録する日時（ISO 8601 形式、デフォルト: 2024-12-31T21:22:00）",
    )
    args = parser.parse_args()

    settings = Settings()  # type: ignore[call-arg]
    credential = settings.cosmos_key or DefaultAzureCredential()
    async with CosmosClient(settings.cosmos_endpoint, credential=credential) as client:
        database = await client.create_database_if_not_exists(
            settings.cosmos_database_name
        )
        container = await database.create_container_if_not_exists(  # type: ignore[union-attr]
            id=CONTAINER_NAME,
            partition_key=PartitionKey(path="/id"),
        )

        item_id = "datetime-demo-1"

        # 既存アイテムがあれば削除
        try:
            await container.delete_item(item=item_id, partition_key=item_id)  # type: ignore[union-attr]
        except CosmosResourceNotFoundError:
            pass

        # datetime -> float に変換して登録
        model = DemoDatetimeModel(
            id=item_id,
            datetime=datetime.fromisoformat(args.datetime_str),
        )
        cosmos_model = DemoDatetimeCosmosModel.model_validate(model)
        await container.create_item(body=cosmos_model.model_dump())  # type: ignore[union-attr]
        print(f"Created: id={item_id}, stored as float={cosmos_model.datetime}")

        # 読み取って float -> datetime に復元
        read_item = await container.read_item(item=item_id, partition_key=item_id)  # type: ignore[union-attr]
        read_model = DemoDatetimeModel.model_validate(read_item)
        print(
            f"Read:    id={read_model.id}, datetime={read_model.datetime.isoformat()}"
        )


if __name__ == "__main__":
    asyncio.run(main())
