# Cosmos DB CRUD サンプル

Azure Cosmos DB (NoSQL API) の CRUD 操作サンプルプロジェクト。

## 開発環境

- Python 3.14+, パッケージマネージャ: uv, タスクランナー: mise
- ローカル開発: Cosmos DB Emulator (vnext-preview) を Docker で起動

## コマンド

- セットアップ: `uv sync`
- エミュレータ起動: `docker compose up -d`
- 実行: `uv run create_item.py`, `uv run read_items.py`, `uv run update_item.py`, `uv run delete_item.py`
- フォーマット・Lint修正: `mise run fix`
- フォーマット・Lint・型チェック: `mise run check`

## 構成

- `settings.py` — 環境変数の読み込み（pydantic-settings）
- `compose.yaml` — Cosmos DB Emulator コンテナ定義
- 各スクリプトは独立して実行可能
