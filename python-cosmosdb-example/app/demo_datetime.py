"""
# 日時の扱い

Azure Cosmos DBのクライアントは内部でjson.dumps()を呼び出しているようなので、datetimeは登録できない。

## 実行

```
uv run -m app.demo_datetime
```

"""

import asyncio
from datetime import datetime
from logging import INFO, StreamHandler, getLogger

from azure.cosmos import PartitionKey
from azure.cosmos.aio import CosmosClient
from azure.cosmos.exceptions import CosmosResourceNotFoundError
from pydantic import BaseModel, model_validator

from .demo import settings

# basicConfig(level=INFO)

logger = getLogger("demo_datetime")
logger.addHandler(StreamHandler())
logger.setLevel(INFO)


class DemoDatetimeModel(BaseModel):
    id: str
    datetime: datetime


class DemoDatetimeCosmosModel(BaseModel):
    id: str
    datetime: float

    @model_validator(mode="before")
    @classmethod
    def convert_datetime_to_float(cls, values):
        if isinstance(values, dict):
            v = values
        elif isinstance(values, BaseModel):
            v = values.model_dump()
        else:
            raise
        if "datetime" in v and isinstance(v["datetime"], datetime):
            # datetime型をエポック秒のfloat型に変換
            v["datetime"] = v["datetime"].timestamp()
        return v


async def main() -> None:
    logger.info("Cosmos DB endpoint: %s", settings.cosmos_endpoint)
    async with CosmosClient(
        url=settings.cosmos_endpoint,
        credential=settings.cosmos_key,
        connection_verify=False,
    ) as cosmos_client:
        db = await cosmos_client.create_database_if_not_exists(
            id=settings.cosmos_database,
        )
        container = await db.create_container_if_not_exists(
            id="demo_datetime", partition_key=PartitionKey("/id")
        )
        try:
            await container.delete_item(item="a", partition_key="a")
        except CosmosResourceNotFoundError:
            pass

        model = DemoDatetimeModel(
            id="a",
            datetime=datetime.fromisoformat("2024-12-31T21:22:00"),
        )

        logger.info("datetimeを登録する")
        cosmos_model = DemoDatetimeCosmosModel.model_validate(model)
        await container.create_item(cosmos_model.model_dump())

        logger.info("アイテムを読み取ってfloatで表現されているdatetimeを変換する")
        read_item = await container.read_item(item="a", partition_key="a")
        read_model = DemoDatetimeModel.model_validate(read_item)
        logger.info("%s", read_model.model_dump_json())


if __name__ == "__main__":
    asyncio.run(main())
