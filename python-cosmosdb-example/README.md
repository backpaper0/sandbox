# Python Cosmos DB Example

## DockerでCosmos Emulatorを起動する

```bash
poetry run task start-cosmos
```

## Containerを作成する

```bash
python create-container.py
```

## Itemを作成する

```bash
python create-items.py
```

`id`と`partition key`の組み合わせが重複した場合、`CosmosResourceExistsError`が発生するっぽい。

## Itemを検索する

```bash
python query-items.py
```

## Itemを削除する

```bash
python delete-items.py
```

削除対象のドキュメントが存在していなくても特にエラーにならないみたい。

## Itemを更新する

```bash
python upsert-items.py
```

`patch_item`メソッドでドキュメントの部分的な更新もできる。

```bash
python patch-items.py
```

- 参考）[Azure Cosmos DB の部分的ドキュメント更新](https://learn.microsoft.com/ja-jp/azure/cosmos-db/partial-document-update)
