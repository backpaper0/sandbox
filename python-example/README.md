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

### 参考資料

- [公式ドキュメント](https://docs.python.org/ja/3/index.html)
