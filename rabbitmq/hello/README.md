# Hello World

```sh
docker run -d --name mq -h usaq -p 5672:5672 rabbitmq
```

```sh
mvn -P consumer exec:java
```

```sh
mvn -P producer exec:java
# mvn -P producer exec:java -Dexec.arguments=10
```

