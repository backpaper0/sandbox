# Cosmos DBローカルエミュレーターをコンテナーで動かす

## 起動と証明書まわりの準備

起動します。

```bash
docker compose up -d
```

証明書を取得します。

```bash
curl -o emulatorcert.crt -v -k https://localhost:8081/_explorer/emulator.pem
```

この時点ではエミュレーターのSSL証明書を検証できないため、`-k`オプション（`--insecure`オプション）を付けています。

なお、先に述べたように接続可能になるまでそこそこ時間がかかるため、次のようなエラーが出ることがあります。その場合は少し待ってリトライします。

```
curl: (35) OpenSSL SSL_connect: SSL_ERROR_SYSCALL in connection to localhost:8081 
```

専用のキーチェーンを作ります。

```bash
security create-keychain -p pass123- cosmos.keychain-db
```

キーチェーンへ証明書をインポートします。

```bash
security import emulatorcert.crt -k cosmos.keychain-db
```

この証明書を常に信頼するようにします。

```bash
security add-trusted-cert -d -r trustRoot -k cosmos.keychain-db emulatorcert.crt
```

## 後始末

コンテナーを破棄します。

```bash
docker compose down -v
```

キーチェーンを削除します。

```bash
security delete-keychain cosmos.keychain-db
```

エクスポートした証明書を削除します。

```bash
rm emulatorcert.crt
```

## Webコンソール

- https://localhost:8081/_explorer/index.html

## 参考情報

- [Azure Cosmos DB エミュレーターを使用したローカルでの開発](https://learn.microsoft.com/ja-jp/azure/cosmos-db/how-to-develop-emulator?tabs=docker-linux%2Cpython&pivots=api-nosql)
