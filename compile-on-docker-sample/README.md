# JavaコードをDocker上でビルドするサンプル

例えば未だリリースされていない（2016年10月現在）Java 9でビルドする。

```
docker run --rm -v "`pwd`:/tmp/build" openjdk:9 javac -d /tmp/build /tmp/build/Hello.java
```
