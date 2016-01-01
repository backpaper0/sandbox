# CDIでアプリケーションサーバ管理リソースを使うための勉強

Payara Microで動かす。

```
gradle run
```

curlした結果。

```
% curl http://localhost:8080/api/print
com.sun.enterprise.container.common.impl.EntityManagerWrapper@340351507
org.glassfish.enterprise.concurrent.ManagedExecutorServiceAdapter@194696075
com.sun.gjc.spi.jdbc40.DataSource40@304246829
% curl http://localhost:8080/api/print
com.sun.enterprise.container.common.impl.EntityManagerWrapper@632422765
org.glassfish.enterprise.concurrent.ManagedExecutorServiceAdapter@194696075
com.sun.gjc.spi.jdbc40.DataSource40@304246829
% curl http://localhost:8080/api/print
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

