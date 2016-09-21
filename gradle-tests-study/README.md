# Gradleで実施するテストを指定するサンプル

`--tests`で指定できる。

特定のテストクラスを実施する。

```
gradlew clean test --tests foo.bar.FooTest
```

ワイルドカードも使えるっぽい。

```
gradlew clean test --tests "*.FooTest"
```

特定のメソッドを実施することもできる。

```
gradlew clean test --tests foo.bar.FooTest.testFoo1
```

こっちもワイルドカード使えるっぽい。

```
gradlew clean test --tests "*.test*1"
```

`--tests`は複数並べることもできる。

```
gradlew clean test --tests "*.FooTest" --tests "*.testBaz1"
```
