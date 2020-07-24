# websocket-redis-pubsub-example

```
MAVEN_OPTS=-Xmx128m mvn -e clean compile jetty:run
```

```
MAVEN_OPTS=-Xmx128m mvn -e jetty:run -Djetty.port=8081
```

```
wscat -c http://localhost:8080/chat
```

```
wscat -c http://localhost:8081/chat
```

