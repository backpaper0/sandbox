# Python Example

- 環境
    - Python 3.11.6

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

> [!NOTE]
> [nbconvert](https://github.com/jupyter/nbconvert)を使用するとノートブックをコマンドラインで実行できます。
> 
> ```
> poetry run jupyter nbconvert --to notebook --execute notebooks/collection.ipynb --output collection.ipynb
> ```

## 静的解析

```sh
poetry run mypy scripts/verify_error.py
```

[taskipy](https://github.com/taskipy/taskipy)でタスク化しているので次のコマンドでも動きます。

```sh
poetry run task verify
```

## テストの実行

```sh
poetry run task test
```

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
