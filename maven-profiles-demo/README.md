
```
mvn dependency:tree
```

```
[INFO] com.example:maven-profiles-demo:jar:1.0-SNAPSHOT
[INFO] +- org.seasar.doma:doma:jar:2.0.0:compile
[INFO] +- com.h2database:h2:jar:1.4.200:compile
[INFO] \- jakarta.ws.rs:jakarta.ws.rs-api:jar:3.0.0:compile
```

---

```
mvn dependency:tree -Pbaz
```

```
[INFO] com.example:maven-profiles-demo:jar:1.0-SNAPSHOT
[INFO] +- org.seasar.doma:doma:jar:2.0.0:compile
[INFO] \- javax.ws.rs:javax.ws.rs-api:jar:2.1.1:compile
```

---

```
mvn dependency:tree -Pfoo,baz
```

```
[INFO] com.example:maven-profiles-demo:jar:1.0-SNAPSHOT
[INFO] +- org.seasar.doma:doma:jar:2.0.0:compile
[INFO] +- com.h2database:h2:jar:1.4.200:compile
[INFO] \- javax.ws.rs:javax.ws.rs-api:jar:2.1.1:compile
```

