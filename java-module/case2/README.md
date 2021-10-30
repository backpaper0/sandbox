![](../assets/plantuml/case2.svg)

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

```sh
jar -cf lib/foo.jar -C bin/foo .
```

```sh
java -p lib -m com.example.foo/com.example.foo.Foo
```

