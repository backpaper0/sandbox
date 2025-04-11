# Python Example

- 環境
    - Python 3.11.6
    - uv 0.6.14

## 準備

```sh
uv sync
```

## 実行

```sh
uv run scripts/hello.py
```

> [!NOTE]
> ノートブックは`.ipynb`ファイルを開いて実行してください。

> [!NOTE]
> [nbconvert](https://github.com/jupyter/nbconvert)を使用するとノートブックをコマンドラインで実行できます。
> 
> ```
> poetry run jupyter nbconvert --to notebook --execute notebooks/collection.ipynb --output collection.ipynb
> ```

## 型チェックを試す

```sh
poetry run task check-type
```

## テストの実行を試す

```sh
poetry run task test
```

## OpenTelemetryを試す

[OpenTelemetryのPython SDK](https://opentelemetry.io/docs/languages/python/)を試します。

コードは [scripts/server.py](scripts/server.py) です。

可視化は[SigNoz](https://signoz.io/)を使うのが手軽だと思います。

### 動作確認の手順

- [SigNozをDocker Composeで起動する](https://github.com/SigNoz/signoz/tree/develop/deploy)
- アプリケーションを起動する
  ```
  uv run task serve
  ```
- 別のコンソールでリクエストを投げる
  ```bash
  uv run task request
  ```
- テレメトリーを確認する
  - http://localhost:3301
  - https://frontend.clickhouse-setup.orb.local/ ([OrbStack](https://orbstack.dev/)使ってるならこっち)

## Appendix

### インストールとか諸々

こんなことやりました。

```sh
asdf install python 3.11.6
```

```sh
asdf local python 3.11.6
```

```sh
pip install uv
```

```sh
uv init
```

```sh
uv add python-dotenv
```

VSCodeで型チェックを行うために次の拡張をインストールしました。

- [Mypy Type Checker](https://marketplace.visualstudio.com/items?itemName=ms-python.mypy-type-checker)

### 参考資料

- [公式ドキュメント](https://docs.python.org/ja/3/index.html)
- [SQLAlchemy(ORM)](https://docs.sqlalchemy.org/en/20/)
- [Alembic(DBマイグレーション)](https://alembic.sqlalchemy.org/en/latest/)