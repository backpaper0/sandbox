![](../assets/plantuml/case1.svg)

```sh
javac -d bin/bar src/bar/*.java
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
java -p lib -m mod.foo/com.example.foo.Foo
```

