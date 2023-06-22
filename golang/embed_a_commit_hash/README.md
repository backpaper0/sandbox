# バイナリにコミットハッシュを埋め込む

```bash
go build -ldflags "-X main.revision=$(git rev-parse @)"
```

