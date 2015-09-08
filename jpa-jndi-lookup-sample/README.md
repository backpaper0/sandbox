# EntityManagerをJNDI lookupするサンプル

## 実行方法

GradleタスクでPayara Microを実行する。

```
gradle run
```

curlでデータを作ったり参照したり。

```
curl http://localhost:8080/memo/1 -d helloworld
curl http://localhost:8080/memo/1
```

## 参考資料

* [JSR 338: Java Persistence API, Version 2.1](https://jcp.org/en/jsr/detail?id=338)の 7.2.1 Obtaining an Entity Manager in the Java EE Environment

