# parentを持つ場合のコンテナ化の例

## 準備

まずparentをローカルリポジトリへ格納する。

```
$ mvn -f hello-parent/pom.xml install
```

## コンテナイメージのビルド

### タグ

素直にやってみる。

```
$ mvn -f hello/pom.xml package jib:dockerBuild
```

`1.0`(アーティファクトのバージョン)と`latest`(parentの`pom.xml`で設定したタグ)が登録されている。

```
$ docker images | grep hello
hello                         1.0                 374c5b49518c        50 years ago        442MB
hello                         latest              374c5b49518c        50 years ago        442MB
```

一旦消して……

```
$ docker rmi $(docker images | grep hello | awk '{print $1 ":" $2}')
```

次はタグを指定してみる。

```
$ mvn -f hello/pom.xml package jib:dockerBuild -Djib.to.tags=xxx,yyy
```

`1.0`と`xxx`、`yyy`が登録された。

```
$ docker images | grep hello
hello                         1.0                 374c5b49518c        50 years ago        442MB
hello                         xxx                 374c5b49518c        50 years ago        442MB
hello                         yyy                 374c5b49518c        50 years ago        442MB
```

### ベースになるイメージ

次はベースになるイメージを変更してみる。
比較するためにタグを変更してイメージを用意する。

まずはそのまま。

```
$ mvn -f hello/pom.xml package jib:dockerBuild -Djib.to.tags=adoptopenjdk
```

次はベースになるイメージを変更。

```
$ mvn -f hello/pom.xml package jib:dockerBuild -Djib.from.image=tomcat:9.0.33-jdk11-openjdk-slim -Djib.to.tags=openjdk
```

それぞれシステムプロパティ`java.vendor`を確認してみる。

```
$ docker run --rm -it hello:adoptopenjdk bash -c "echo 'System.out.println(System.getProperty(\"java.vendor\"))' | jshell -s"
Apr 09, 2020 8:01:47 AM java.util.prefs.FileSystemPreferences$1 run
INFO: Created user preferences directory.
-> System.out.println(System.getProperty("java.vendor"))
AdoptOpenJDK
->
```

```
$ docker run --rm -it hello:openjdk bash -c "echo 'System.out.println(System.getProperty(\"java.vendor\"))' | jshell -s"
Apr 09, 2020 8:01:39 AM java.util.prefs.FileSystemPreferences$1 run
INFO: Created user preferences directory.
-> System.out.println(System.getProperty("java.vendor"))
Oracle Corporation
->
```

ベースになるイメージは変更されている様子。

