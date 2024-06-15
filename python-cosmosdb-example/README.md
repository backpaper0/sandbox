# Python Cosmos DB Example

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
