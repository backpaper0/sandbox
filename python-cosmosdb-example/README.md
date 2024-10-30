# Python Cosmos DB Example

## DockerでCosmos Emulatorを起動する

```bash
docker compose up -d
```

## Containerを作成する

```bash
poetry run python -m app.create-container
```

## Itemを作成する

```bash
poetry run python -m app.create-items
```

`id`と`partition key`の組み合わせが重複した場合、`CosmosResourceExistsError`が発生するっぽい。

## Itemを検索する

```bash
poetry run python -m app.query-items
```

## Itemを削除する

```bash
poetry run python -m app.delete-items
```

削除対象のドキュメントが存在していなくても特にエラーにならないみたい。

## Itemを更新する

```bash
poetry run python -m app.upsert-items
```

`patch_item`メソッドでドキュメントの部分的な更新もできる。

```bash
poetry run python -m app.patch-items
```

- 参考）[Azure Cosmos DB の部分的ドキュメント更新](https://learn.microsoft.com/ja-jp/azure/cosmos-db/partial-document-update)
