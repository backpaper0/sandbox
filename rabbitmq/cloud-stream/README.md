# Spring Cloud Stream

## RabbitMQを起動する(via Docker)

管理画面を見たいので`-management`が付いているバージョンを使用する。

```sh
docker run -d --name mq -h usaq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

次のURLで管理画面を開く。

- http://localhost:15672/

ユーザー名・パスワードはデフォルトだとどちらも`guest`。

## メッセージ送信側アプリケーションを起動する

HTTPで受け取った名前を`Person`にセットしてキューへ送信するアプリケーション。

```sh
cd source-app
mvnw package
java -jar target/source-app.jar
```

## メッセージ受信側アプリケーションを起動する

キューから受信したメッセージを標準出力へ書き出すアプリケーション。

```sh
cd sink-app
mvnw package
java -jar target/sink-app.jar
# 複数起動する時はポートを変更すること
# java -jar target/sink-app.jar --server.port=8082
```

## メッセージを送信する

`source-app`へHTTPで`name`を送る。

```sh
curl localhost:8080 -d name=hoge
```

そうすると、`SourceApp#handle`がHTTPリクエストを受け取って`sample` exchangeへメッセージを送信する。

ここで送信先となるexchangeは`application.properties`に書かれた`spring.cloud.stream.bindings.output.destination`の値で設定される。
デフォルトだとbinding target name、つまり今回だと`output`になる。

exchangeへ送信されたメッセージはバインドされているキューへ送信される。

キューは`sink-app`のインスタンス毎に1つ用意される。
どのexchangeへバインドされるかは`application.properties`に書かれた`spring.cloud.stream.bindings.input.destination`の値で設定される。
デフォルトだとbinding target name、つまり今回だと`input`になる。

`SinkApp#handle`に受信したメッセージが渡され、標準出力に書き出される。

### 参考

- binding target name: `org.springframework.cloud.stream.annotation.Output.value()`
- binding target name: `org.springframework.cloud.stream.annotation.Input.value()`
- `org.springframework.cloud.stream.config.BindingServiceProperties`
- `org.springframework.cloud.stream.config.BindingProperties`

