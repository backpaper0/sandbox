# Langfuse example

LangGraphによるエージェントのトレースをLangfuseで可視化する例です。

## 概要

このプロジェクトは以下の構成になっています：

- **main.py**: LangGraphを使ったエージェントの例（[LangGraphのGet startedを拝借](https://langchain-ai.github.io/langgraph/#get-started)）
- **get_weather**: 天気情報を取得するツール（モック）
- **Langfuse**: エージェントの実行ログとトレースを記録・可視化

## 準備

### 1. Langfuseサーバーの起動

```bash
git clone git@github.com:langfuse/langfuse.git
cd langfuse
docker compose up -d
```

### 2. Langfuseの初期設定

1. <http://localhost:3000> を開いてサインアップ
2. organizationとプロジェクトを作成
3. Settings > API Keys でAPIキーを生成

### 3. 環境変数の設定

```bash
cp .env.example .env
```

`.env`ファイルを編集して、生成したAPIキーを設定：

```
LANGFUSE_PUBLIC_KEY=pk-xxxxx
LANGFUSE_SECRET_KEY=sk-xxxxx
LANGFUSE_HOST=http://localhost:3000
```

### 4. 依存関係のインストール

```bash
uv sync
```

## 実行

```bash
uv run main.py
```

実行後、Langfuseの管理画面（<http://localhost:3000>）でトレースを確認できます。

## 確認できるトレース情報

- エージェントの思考プロセス
- ツール（`get_weather`）の呼び出し
- レスポンス生成の流れ
- 実行時間とコスト（モデル使用量）
