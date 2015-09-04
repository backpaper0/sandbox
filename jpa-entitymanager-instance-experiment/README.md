# EntityManagerをApplicationScopedなCDI管理ビーンにインジェクションできるのか試す

## 概要

`@PersistenceContext` を付けてインジェクションした `EntityManager`
のインスタンスをマルチスレッドで使えるのか確認するための実験コードです。

マルチスレッドで使えることが分かればどのスコープのCDI管理ビーンにも安心してインジェクションできます。

## 確認方法

`@ApplicationScoped` を付けたリソースクラスを用意して `@PersistenceContext`
で `EntityManager` をインジェクションしています。
リソースメソッドではインジェクションした `EntityManager` そのものと
`EntityManager.getDelegate` で取得したインスタンスに対してクラス名とハッシュコードを繋げた文字列を返しています。
なお `EntityManager.getDelegate` は3回呼び出しています。

このAPIを何度か叩いてハッシュコードを確認します。
次の事柄を確認できたら `EntityManager` を `ApplicationScoped`
なCDI管理ビーンにインジェクションしても大丈夫だと言えると思います。

* 同一リクエストでは同じハッシュコード、つまり同じインスタンスが返っている
* 異なるリクエストでは異なるハッシュコード、つまり異なるインスタンスが返っている

## 確認手順

まずPayara Microで実験用アプリケーションを起動します。
Gradleタスクを書いているので次のコマンドで起動できます。

```
gradle run
```

次にcurlを叩きます。

```
curl http://localhost:8080/sample/
```

## 結果

1回目。

```
com.sun.enterprise.container.common.impl.EntityManagerWrapper@1812976192
org.eclipse.persistence.internal.jpa.EntityManagerImpl@1706186126
org.eclipse.persistence.internal.jpa.EntityManagerImpl@1706186126
org.eclipse.persistence.internal.jpa.EntityManagerImpl@1706186126
```

2回目。

```
com.sun.enterprise.container.common.impl.EntityManagerWrapper@1812976192
org.eclipse.persistence.internal.jpa.EntityManagerImpl@1051597900
org.eclipse.persistence.internal.jpa.EntityManagerImpl@1051597900
org.eclipse.persistence.internal.jpa.EntityManagerImpl@1051597900
```

3回目。

```
com.sun.enterprise.container.common.impl.EntityManagerWrapper@1812976192
org.eclipse.persistence.internal.jpa.EntityManagerImpl@345443677
org.eclipse.persistence.internal.jpa.EntityManagerImpl@345443677
org.eclipse.persistence.internal.jpa.EntityManagerImpl@345443677
```

## まとめ

結果をご覧の通り、CDI管理ビーンに実際にインジェクションされているのは
`EntityManagerWrapper` というラッパーで、
実際は `EntityManagerImpl` が使われているようです。
そして `EntityManagerWrapper` から取得される `EntityManagerImpl`
は同一リクエスト内では同一インスタンスですが、異なるリクエストでは異なるインスタンスとなるようです。

これで安心してインジェクションしまくれますね！



