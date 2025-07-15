# PythonでAWS Lambda (デプロイはDockerイメージ)の例

## ビルド

```bash
docker build -t python-lambda-docker-example .
```

## 起動

```bash
docker run -it --rm -p 8080:8080 python-lambda-docker-example
```

起動時のログ。

```
15 Jul 2025 11:37:48,072 [INFO] (rapid) exec '/var/runtime/bootstrap' (cwd=/var/task, handler=)
```

## 関数の実行

```bash
curl "http://localhost:8080/2015-03-31/functions/function/invocations" --json '{}'
```

レスポンス。

```json
{"statusCode": 200, "body": "Hello from Dockerized Lambda!"}
```

サーバーのログ。

```
15 Jul 2025 11:39:16,895 [INFO] (rapid) The extension's directory "/opt/extensions" does not exist, assuming no extensions to be loaded.
15 Jul 2025 11:39:16,895 [INFO] (rapid) Starting runtime without AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_SESSION_TOKEN , Expected?: false
15 Jul 2025 11:39:16,949 [INFO] (rapid) INIT RTDONE(status: success)
15 Jul 2025 11:39:16,949 [INFO] (rapid) INIT REPORT(durationMs: 54.610000)
15 Jul 2025 11:39:16,949 [INFO] (rapid) INVOKE START(requestId: 08c10173-520f-42f1-b7f8-4039fb480e93)
15 Jul 2025 11:39:16,951 [INFO] (rapid) INVOKE RTDONE(status: success, produced bytes: 0, duration: 1.853000ms)
END RequestId: 08c10173-520f-42f1-b7f8-4039fb480e93
REPORT RequestId: 08c10173-520f-42f1-b7f8-4039fb480e93	Init Duration: 0.10 ms	Duration: 56.56 ms	Billed Duration: 57 ms	Memory Size: 3008 MB	Max Memory Used: 3008 MB
```

## 参考情報

- https://docs.aws.amazon.com/ja_jp/lambda/latest/dg/lambda-python.html
- https://github.com/aws/aws-lambda-runtime-interface-emulator


