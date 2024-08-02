# PythonでSelenium

ブラウザをデバッグモードで起動して、そこに接続する方式を試す。

デバッグモードでの起動コマンドはこちら。

```bash
chrome --remote-debugging-port=9222 --user-data-dir="$HOME/selenium-profile"
```

Macはこんな感じ。

```bash
/Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --remote-debugging-port=9222 --user-data-dir="$HOME/selenium-profile"
```

プログラムの起動コマンドはこちら。

```bash
python -m app.selenium_example
```

```bash
python -m app.splinter_example
```

## Scrapyと組み合わせてクローリングする

```bash
scrapy runspider app/scrapy_example.py -O example.jsonl
```
