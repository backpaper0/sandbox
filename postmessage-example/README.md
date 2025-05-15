```bash
python -m http.server -d sender 8000
```

```bash
python -m http.server -d receiver 3000
```

http://localhost:8000 を開いてボタンを押す。

http://localhost:3000 でメッセージを受け取って表示する。


https://developer.mozilla.org/ja/docs/Web/API/Window/postMessage
