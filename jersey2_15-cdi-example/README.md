# Jersey 2.15 + CDI のサンプル

## 概要

Jersey 2.15からCDIとの連携機能がJava EE環境以外でも動くように
なったらしいので試してみました。

* https://github.com/jersey/jersey/releases/tag/2.15

## 動かし方

```
mvn exec:java
```

サーバーが起動したら次のURLにアクセスしてください。

* http://localhost:8080/hello?name=YourName
