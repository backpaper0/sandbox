# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

Azure Queue Storage のメッセージ送受信サンプル。ローカル開発には Azurite エミュレーターを使用する。

## 開発環境

- Python 3.14+ / uv / Docker
- ランタイム管理: mise (`mise.toml`)
- 設定管理: pydantic-settings (`.env` ファイルから読み込み)
- 認証: `QUEUE_ACCOUNT_KEY` があればキー認証、なければ `DefaultAzureCredential`

## コマンド

```sh
# セットアップ
uv sync
cp .env.example .env
docker compose up -d   # Azurite 起動

# 実行
uv run enqueue.py      # メッセージ送信
uv run dequeue.py      # メッセージ受信

# Lint / フォーマット (mise 経由)
mise run fix           # ruff format + ruff check --fix
mise run check         # ruff format --check + ruff check + ty check

# 個別ファイル指定
mise run fix -- enqueue.py
mise run check -- settings.py

# mise なしで直接実行する場合
uvx ruff format .
uvx ruff check --fix .
uvx --with-requirements pyproject.toml ty check .
```

## アーキテクチャ

- `settings.py`: `pydantic_settings.BaseSettings` で環境変数をバリデーション付きで読み込む設定クラス
- `enqueue.py` / `dequeue.py`: それぞれ独立したスクリプト。`Settings` から設定を取得し `QueueClient` を構築して操作する
- `compose.yaml`: Azurite コンテナ (Blob: 10000, Queue: 10001, Table: 10002)
