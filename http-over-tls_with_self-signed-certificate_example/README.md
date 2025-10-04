# HTTP over TLS w/ 自己署名証明書

## 環境構築

```bash
./setup.sh
```

## 動作確認

```bash
docker compose up -d nginx
```

### curl

```bash
docker compose run --rm curl
```

### Python

```bash
docker compose run --rm uv-requests
```
