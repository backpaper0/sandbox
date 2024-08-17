コスト計算。

```
python -m app.main --cost -i data/input.jsonl
```

埋め込みの生成。

```
python -m app.main -i data/input.jsonl -o data/output.jsonl
```

DBへの埋め込みインポート。

```
python -m app.main -I -i data/output.jsonl
```

静的解析など。

```
poetry run task lint
```

```
poetry run task fix
```