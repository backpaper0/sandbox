# Cosmos DB CRUD サンプル

Azure Cosmos DB (NoSQL API) の基本的な CRUD 操作を行う Python サンプルです。

## セットアップ

```bash
uv sync
cp .env.example .env
docker compose up -d
```

## 使い方

```bash
# アイテム作成
uv run create_item.py
uv run create_item.py '{"id": "2", "category": "gear", "name": "Gloves", "quantity": 3}'

# アイテム一覧
uv run read_items.py
uv run read_items.py --query "SELECT * FROM c WHERE c.category = 'gear'"

# アイテム更新（パッチ操作）
uv run update_item.py 1 gear '[{"op": "replace", "path": "/quantity", "value": 10}]'

# アイテム削除
uv run delete_item.py 1 gear
```

## 構成

| ファイル | 説明 |
|---|---|
| `compose.yaml` | Cosmos DB Emulator コンテナ定義 |
| `settings.py` | 環境変数の読み込み |
| `create_item.py` | データベース・コンテナ・アイテムの作成 |
| `read_items.py` | アイテムのクエリ |
| `update_item.py` | アイテムのパッチ更新 |
| `delete_item.py` | アイテムの削除 |
