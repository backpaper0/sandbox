# Azure Queue Storage サンプル

Azure Queue Storage の基本的なメッセージ送受信を試すサンプルプロジェクト。ローカル開発には [Azurite](https://learn.microsoft.com/ja-jp/azure/storage/common/storage-use-azurite) エミュレーターを使用する。

## 必要なもの

- Python 3.14+
- [uv](https://docs.astral.sh/uv/)
- Docker (Azurite 実行用)

## セットアップ

```sh
# 依存パッケージのインストール
uv sync

# 環境変数の設定
cp .env.example .env

# Azurite の起動
docker compose up -d
```

## 使い方

```sh
# メッセージをキューに送信
uv run enqueue.py

# メッセージをキューから受信
uv run dequeue.py
```

## 構成

| ファイル | 説明 |
| --- | --- |
| `enqueue.py` | キューにメッセージを送信する |
| `dequeue.py` | キューからメッセージを受信・削除する |
| `settings.py` | 環境変数から設定を読み込む (pydantic-settings) |
| `compose.yaml` | Azurite コンテナの定義 |
