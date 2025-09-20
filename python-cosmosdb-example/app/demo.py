"""
# demo

新しいCosmos DBエミュレーターを試すデモコード。

## 実行

```
docker compose up -d
```

```
uv run -m app.demo
```

```
docker compose down
```

## コンテナ

### コンテナビルド

```
pack build demo
```

### コンテナ実行

```
GATEWAY_PUBLIC_ENDPOINT=cosmos docker compose up -d
```

```
docker compose run --rm demo
```

```
docker compose down
```

"""

import asyncio
from logging import INFO, StreamHandler, getLogger
from typing import Any, Literal

from azure.cosmos import PartitionKey
from azure.cosmos.aio import CosmosClient
from pydantic import BaseModel
from pydantic_settings import BaseSettings, SettingsConfigDict
from ulid import ULID

# basicConfig(level=INFO)

logger = getLogger("demo")
logger.addHandler(StreamHandler())
logger.setLevel(INFO)


class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=".env",
        extra="ignore",
    )
    cosmos_endpoint: str
    cosmos_key: str
    cosmos_database: str


settings = Settings()  # type: ignore


class Food(BaseModel):
    id: str
    name: str
    ingredients: list[str] | None = None
    category: str


class PatchOperation(BaseModel):
    op: Literal["add", "remove", "set", "incr", "add", "move"]
    path: str
    value: Any


# 現段階ではCosmos DBのエミュレーターが日本語を保存できなさそうなので、ChatGPTくんに頼んで英語に。
# プロンプトは「次のPythonで書かれたサンプルデータを英語にして。」
# foods = [
#     Food(id=str(ULID()), name="麻婆豆腐", ingredients=["豆腐", "ひき肉", "ニラ"], category="辛い料理"),
#     Food(id=str(ULID()), name="回鍋肉", ingredients=["豚肉", "ピーマン", "ニンニク"], category="炒め物"),
#     Food(id=str(ULID()), name="酢豚", ingredients=["豚肉", "パイナップル", "ピーマン"], category="甘酢料理"),
#     Food(id=str(ULID()), name="餃子", ingredients=["豚肉", "キャベツ", "ニラ"], category="点心"),
#     Food(id=str(ULID()), name="炒飯", ingredients=["米", "卵", "ネギ", "ハム"], category="炒め物"),
#     Food(id=str(ULID()), name="エビチリ", ingredients=["エビ", "ピーマン", "ニンニク", "唐辛子"], category="辛い料理"),
#     Food(id=str(ULID()), name="油淋鶏", ingredients=["鶏肉", "キュウリ", "ニンニクソース"], category="揚げ物"),
# ]
foods = [
    Food(id=str(ULID()), name="Mapo Tofu", category="Spicy Dish"),
    Food(
        id=str(ULID()),
        name="Hoisin Pork",
        ingredients=["Pork", "Bell Pepper", "Garlic"],
        category="Stir-Fry",
    ),
    Food(
        id=str(ULID()),
        name="Sweet and Sour Pork",
        ingredients=["Pork", "Pineapple", "Bell Pepper"],
        category="Sweet and Sour Dish",
    ),
    Food(
        id=str(ULID()),
        name="Gyoza",
        ingredients=["Pork", "Cabbage", "Chives"],
        category="Dim Sum",
    ),
    Food(
        id=str(ULID()),
        name="Fried Rice",
        ingredients=["Rice", "Egg", "Green Onion", "Ham"],
        category="Stir-Fry",
    ),
    Food(
        id=str(ULID()),
        name="Chili Shrimp",
        ingredients=["Shrimp", "Bell Pepper", "Garlic", "Chili Pepper"],
        category="Spicy Dish",
    ),
    Food(
        id=str(ULID()),
        name="Yurinchi Chicken",
        ingredients=["Chicken", "Cucumber", "Garlic Sauce"],
        category="Fried Dish",
    ),
]


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
            id="foods", partition_key=PartitionKey("/category")
        )

        logger.info("データの取得と削除")
        items = container.query_items("select * from c")
        async for item in items:
            food = Food(**item)
            await container.delete_item(item=food.id, partition_key=food.category)

        logger.info("データの登録")
        for food in foods:
            await container.create_item(food.model_dump(exclude_none=True))

        logger.info("データの部分更新")
        food = foods[0]
        patch_operations = [
            path_operation.model_dump()
            for path_operation in [
                PatchOperation(
                    op="add", path="/ingredients", value=["Tofu", "Ground Meat"]
                ),
                PatchOperation(op="add", path="/ingredients/2", value="Chives"),
            ]
        ]
        patched = await container.patch_item(
            item=food.id, partition_key=food.category, patch_operations=patch_operations
        )
        logger.info("-> %s", patched)
        foods[0] = Food(**patched)

        logger.info("データのIDによる取得")
        read = await container.read_item(
            item=foods[0].id, partition_key=foods[0].category
        )
        logger.info("-> %s", read)


if __name__ == "__main__":
    asyncio.run(main())
