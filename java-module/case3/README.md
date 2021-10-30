![](../assets/plantuml/case3.svg)

```sh
javac -d bin/baz src/baz/*.java
```

```sh
jar -cf lib/baz.jar -C bin/baz .
```

```sh
javac -d bin/bar -p lib src/bar/*.java
```

```sh
jar -cf lib/bar.jar -C bin/bar .
```

```sh
javac -d bin/foo -p lib src/foo/*.java
```

エラーが出る。

```
src/foo/Foo.java:4: エラー: パッケージcom.example.bazは表示不可です
import com.example.baz.Baz;
                  ^
  (パッケージcom.example.bazはモジュールmod.bazで宣言されていますが、モジュールmod.fooに読み込まれていません)
エラー1個
```

`mod.baz`へ依存していることを明示すればいい。
そのためには`src/foo/module-info.java`を次のように修正する。

```java
module mod.foo {
    requires mod.bar;
    requires mod.baz;
}
```

再度コンパイルすると今度は成功する。

```sh
javac -d bin/foo -p lib src/foo/*.java
```

あるいは`mod.bar`の`module-info.java`で`mod.baz`の依存が推移的であると示してもいい。
そのためには`src/bar/module-info.java`を次のように修正する。

```java
module mod.bar {
    exports com.example.bar;
    requires transitive mod.baz;
}
```

こちらの修正でもコンパイルは成功する（もう一度`mod.bar`からビルドする）。

```sh
javac -d bin/bar -p lib src/bar/*.java
jar -cf lib/bar.jar -C bin/bar .
javac -d bin/foo -p lib src/foo/*.java
```

```sh
jar -cf lib/foo.jar -C bin/foo .
```

```sh
java -p lib -m mod.foo/com.example.foo.Foo
```

