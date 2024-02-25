# Python Example

- 環境
    - Python 3.12.2

## 準備

```sh
poetry install
```

## 実行

```sh
poetry run python scripts/hello.py
```

> [!NOTE]
> ノートブックは`.ipynb`ファイルを開いて実行してください。

## 静的解析

```sh
poetry run mypy scripts/verify_error.py
```

## Appendix

### インストールとか諸々

こんなことやりました。

```sh
asdf install python 3.12.2
```

```sh
asdf local python 3.12.2
```

```sh
pip install poetry
```

```sh
poetry init -q
```

```sh
poetry add python-dotenv
```

あと、`.vscode/settings.json`で`python`コマンドと`poetry`コマンドのパスを設定してみた。

VSCodeで型チェックを行うために次の拡張をインストールした。

- [Mypy Type Checker](https://marketplace.visualstudio.com/items?itemName=ms-python.mypy-type-checker)

### 参考資料

- [公式ドキュメント](https://docs.python.org/ja/3/index.html)
