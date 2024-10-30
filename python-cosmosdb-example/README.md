# Python Cosmos DB Example

## DockerでCosmos Emulatorを起動する

```bash
docker compose up -d
```

## Containerを作成する

```bash
poetry run python -m app.create_container
```

## Itemを作成する

```bash
poetry run python -m app.create_items
```

`id`と`partition key`の組み合わせが重複した場合、`CosmosResourceExistsError`が発生するっぽい。

## Itemを検索する

```bash
poetry run python -m app.query_items
```

## Itemを削除する

```bash
poetry run python -m app.delete_items
```

削除対象のドキュメントが存在していなくても特にエラーにならないみたい。

## Itemを更新する

```bash
poetry run python -m app.upsert_items
```

`patch_item`メソッドでドキュメントの部分的な更新もできる。

```bash
poetry run python -m app.patch_items
```

- 参考）[Azure Cosmos DB の部分的ドキュメント更新](https://learn.microsoft.com/ja-jp/azure/cosmos-db/partial-document-update)
