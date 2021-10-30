無名モジュールをルートにしたい場合。

![](../assets/plantuml/case5.svg)

```sh
javac -d bin/bar src/bar/*.java && jar -cf lib/bar.jar -C bin/bar .
```

```sh
javac -d bin/foo -p lib src/foo/*.java
```

```
src/foo/Foo.java:3: エラー: パッケージcom.example.barは表示不可です
import com.example.bar.Bar;
                  ^
  (パッケージcom.example.barはモジュールmod.barで宣言されていますが、モジュール・グラフにありません)
エラー1個
```

`mod.bar`もルートに追加することで`foo`（無名モジュール）から参照できるようにする。

```sh
javac -d bin/foo -p lib --add-modules mod.bar src/foo/*.java
```

```sh
jar -cf lib/foo.jar -C bin/foo .
```

```sh
java -p lib -m foo/com.example.foo.Foo
```

```
Exception in thread "main" java.lang.NoClassDefFoundError: com/example/bar/Bar
        at foo/com.example.foo.Foo.main(Foo.java:7)
Caused by: java.lang.ClassNotFoundException: com.example.bar.Bar
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
        ... 1 more
```

実行時も`mod.bar`をルートに追加する。

```sh
java -p lib --add-modules mod.bar -m foo/com.example.foo.Foo
```
