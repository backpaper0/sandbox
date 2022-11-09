パッケージと実際のディレクトリが一致していなくても`mvn compile`は成功するなぁ。

```
docker run --rm --mount type=bind,src=$(pwd),dst=/workspace \
  --mount type=bind,src=$HOME/.m2,dst=/root/.m2 \
  maven:3-jdk-11 mvn -f /workspace/pom.xml clean compile
```

と思ったけど`javac`も普通に成功するわ。

そういうものだったっけ？

Java 8でも成功するなぁ。

```
docker run --rm --mount type=bind,src=$(pwd),dst=/workspace \
  --mount type=bind,src=$HOME/.m2,dst=/root/.m2 \
  maven:3-jdk-8 mvn -f /workspace/pom.xml clean compile
```

Java 7でも、、、

```
docker run --rm --mount type=bind,src=$(pwd),dst=/workspace \
  --mount type=bind,src=$HOME/.m2,dst=/root/.m2 \
  maven:3-jdk-7 mvn -f /workspace/pom.xml clean compile
```

そういうものらしい。知らなかった。

https://twitter.com/hishidama/status/1266176675768745984

`javac`の`-sourcepath`はパッケージとディレクトリが一致しないと機能しない様子。

これは成功する。

```
# rm -fr target
javac -d target/classes -sourcepath src/main/java src/main/java/com/example/App4.java
```

これは失敗する。

```
# rm -fr target
javac -d target/classes -sourcepath src/main/java src/main/java/com/example/App2.java
```

