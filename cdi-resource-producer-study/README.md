# CDIでアプリケーションサーバ管理リソースを使うための勉強

Payara Microで動かす。

```
gradle run
```

curlした結果。

```
% curl http://localhost:8080/sample/api/print
com.sun.enterprise.container.common.impl.EntityManagerWrapper@340351507
org.glassfish.enterprise.concurrent.ManagedExecutorServiceAdapter@194696075
com.sun.gjc.spi.jdbc40.DataSource40@304246829
% curl http://localhost:8080/sample/api/print
com.sun.enterprise.container.common.impl.EntityManagerWrapper@632422765
org.glassfish.enterprise.concurrent.ManagedExecutorServiceAdapter@194696075
com.sun.gjc.spi.jdbc40.DataSource40@304246829
% curl http://localhost:8080/sample/api/print
com.sun.enterprise.container.common.impl.EntityManagerWrapper@1027218037
org.glassfish.enterprise.concurrent.ManagedExecutorServiceAdapter@194696075
com.sun.gjc.spi.jdbc40.DataSource40@304246829
```

`EntityManager` はリクエスト毎にインスタンスが作られている。
`ManagedExecutorService` と `DataSource` は同じインスタンスがインジェクションされている。

というわけで `EntityManager` は `RequestScoped` 、
`ManagedExecutorService` と `DataSource` は `ApplicationScoped` が良さそう。

しかし、jBatchや `ManagedExecutorService` のタスクでも `EntityManager` を使いたいので
`RequestScoped` はダメな気がする。

ステートレスセッションビーンでも試してみた。

```
% curl http://localhost:8080/sample/api/print/ejb
http-listener(6)
app.ResourceBean@634254643
com.sun.enterprise.container.common.impl.EntityManagerWrapper@867432777
org.glassfish.enterprise.concurrent.ManagedExecutorServiceAdapter@417163264
com.sun.gjc.spi.jdbc40.DataSource40@1036485846
concurrent/__defaultManagedExecutorService-managedThreadFactory-Thread-1
app.ResourceBean@634254643
com.sun.enterprise.container.common.impl.EntityManagerWrapper@867432777
org.glassfish.enterprise.concurrent.ManagedExecutorServiceAdapter@417163264
com.sun.gjc.spi.jdbc40.DataSource40@1036485846
```

`EntityManager` も `ApplicationScoped` で良いような気がしてきた。
ラッパークラスっぽいし。

よくよく考えたら[こんな実験してた](https://github.com/backpaper0/sandbox/tree/master/jpa-entitymanager-instance-experiment)。
