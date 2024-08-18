# python-opentelemetry-example

## 動作確認

[OpenObserve](https://openobserve.ai/)を起動する。

```
docker compose up -d
```

アプリケーションを起動する。

```
fastapi dev
```

http://localhost:8000/docs を開いて適当にリクエストを送信する。

OpenObserveでテレメトリーを確認する。

- http://localhost:5080
- [OrbStack](https://orbstack.dev/)を使っている場合は https://openobserve.python-opentelemetry-example.orb.local

ユーザー名とパスワードは次の通り。

- ユーザー名：`root@example.com`
- パスワード：`pass1234`
