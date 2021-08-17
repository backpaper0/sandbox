# pitest-demo

[Pitest](http://pitest.org/)はJavaでミューテーションテストが行えるツール。

まずはバイトコードを生成するためコンパイルを行う(テストコードのコンパイルも含む)。

```
mvn test-compile
```

次にミューテーション解析を行う。
結果は`target/pit-reports`以下に出力される。

```
mvn org.pitest:pitest-maven:mutationCoverage
```

`-DwithHistory`を付けると同じコードベースの解析を繰り返す際に高速化できるらしい。

```
mvn -DwithHistory org.pitest:pitest-maven:mutationCoverage
```
