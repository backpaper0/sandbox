MavenでJava 1.8互換のモジュールJARを作る方法を考えている。

まずライブラリをビルドする。
Java 17とJava 1.8でそれぞれビルドして、`maven-antrun-plugin`で`module-info.class`をJava 1.8のビルド結果にコピーしてからJARを生成してみた。

```sh
JAVA_HOME=$(/usr/libexec/java_home -v 17)  mvn -f bar clean compile -Pmodule && \
JAVA_HOME=$(/usr/libexec/java_home -v 1.8) mvn -f bar install
```

このライブラリを別のプロジェクトから使ってみる。
まずはJava 1.8で使えることを確認する。

```sh
JAVA_HOME=$(/usr/libexec/java_home -v 1.8)  mvn -f foo clean verify
```

次はJava 17でビルドする。
モジュール対応する場合はプロジェクト側に`module-info.java`がないといけない。
そのために用意した`module`プロファイルを使ってビルドする。

```sh
JAVA_HOME=$(/usr/libexec/java_home -v 17)  mvn -f foo clean verify -Pmodule
```

そうすると次のエラーが出るはず。

```
[ERROR] /path/to/sandbox/java-module/case6/foo/src/main/java/com/example/foo/Foo.java:[6,65] パッケージcom.example.bar.internalは表示不可です
  (パッケージcom.example.bar.internalはモジュールmod.barで宣言されていますが、エクスポートされていません)
```

`src/main/java/com/example/Foo.java`を次のように修正して再度ビルドしてみる。

```java
package com.example.foo;

public class Foo {
	public static void main(String[] args) {
		System.out.println("Foo, " + new com.example.bar.Bar());
		// System.out.println("Foo, " + new com.example.bar.internal.InternalBar());
	}
}
```

```sh
JAVA_HOME=$(/usr/libexec/java_home -v 17)  mvn -f foo clean verify -Pmodule
```

まだ次のエラーが出るはず。

```
[ERROR] /path/to/sandbox/java-module/case6/foo/src/test/java/com/example/foo/FooTest.java:[14,50] パッケージcom.example.bar.internalは表示不可です
  (パッケージcom.example.bar.internalはモジュールmod.barで宣言されていますが、エクスポートされていません)
```

`src/test/java/com/example/FooTest.java`を次のように修正して再度ビルドしてみる。

```java
package com.example.foo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FooTest {
	@Test
	public void bar() {
		assertNotNull(new com.example.bar.Bar());
	}

	// @Test
	// public void internalBar() {
	// 	assertNotNull(new com.example.bar.internal.InternalBar());
	// }
}
```

```sh
JAVA_HOME=$(/usr/libexec/java_home -v 17)  mvn -f foo clean verify -Pmodule
```

今度こそビルドが通ったはず。
