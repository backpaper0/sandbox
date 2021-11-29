# zookeeper-leadership-example

[Apache ZooKeeper](https://zookeeper.apache.org/)を用いたリーダー選出を試す。

ZooKeeperのクライアントライブラリ[Apache Curator](https://curator.apache.org/)を使用する。

## 起動方法

Docker ComposeでZooKeeperを起動する。

```sh
docker compose up -d
```

アプリケーションをビルドする。

```sh
mvn package
```

任意の数だけアプリケーションを起動する。

```sh
java -jar target/zookeeper-leadership-example-1.0-SNAPSHOT.jar
```

リーダーに選出されたら次のようなログが出力される。

```
[Curator-LeaderSelector-0] INFO com.example.App - Take leadership: f9dccc6b-da90-44e6-9669-649b4ae70b30 - 1
```

リーダー選出は3秒毎に実施するようにしている。

## その他

リーダー選出を実施しているのは次の箇所。

- `org.apache.curator.framework.recipes.locks.StandardLockInternalsDriver.createsTheLock(CuratorFramework, String, byte[])`

