パッケージと実際のディレクトリが一致していなくても`mvn compile`は成功するなぁ。

と思ったけど`javac`も普通に成功するわ。

そういうものだったっけ？

Java 8でも成功するなぁ。

```
docker run --rm --mount type=bind,src=$(pwd),dst=/workspace \
  --mount type=bind,src=$HOME/.m2,dst=/root/.m2,readonly \
  maven:3-jdk-8 mvn -f /workspace/pom.xml clean compile
```

