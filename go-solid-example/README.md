# go-solid-example

フロントエンドにSolid、バックエンドにGoを使う例。

## 構成

開発中はフロントエンドの開発サーバーを起動し、`/api`へのリクエストをバックエンドへproxyする。
実行バイナリーのビルド時はビルドされたフロントエンドを組み込んで実行バイナリーを生成する。

## プロジェクトの準備手順

バックエンドのGoプロジェクトを初期化する。

```bash
go mod init github.com/backpaper0/go-solid-example
```

フロントエンドのSolidプロジェクトを初期化する。

```bash
npx degit solidjs/templates/ts ui
```

フロントエンドにproxyの設定を行う。

```diff
diff --git a/go-solid-example/ui/vite.config.ts b/go-solid-example/ui/vite.config.ts
index 9ff59a1..cdc979b 100644
--- a/go-solid-example/ui/vite.config.ts
+++ b/go-solid-example/ui/vite.config.ts
@@ -5,6 +5,9 @@ export default defineConfig({
   plugins: [solidPlugin()],
   server: {
     port: 3000,
+    proxy: {
+      '/api': 'http://localhost:8080/api'
+    },
   },
   build: {
     target: 'esnext',
```

## 開発時の起動方法

フロントエンドを起動する。

```bash
cd ui
npm run dev
```

バックエンドを起動する。

```bash
go run cmd/main.go
```

次のURLを開く。

- http://localhost:3000/

## ビルド方法

フロントエンドをビルドする。

```bash
cd ui
npm run build
```

バックエンドをビルドする。
このときフロントエンドが実行バイナリーへ組み込まれる。

```bash
go build -o go-solid-example cmd/main.go
```

実行バイナリーを実行する。

```bash
./go-solid-example
```
