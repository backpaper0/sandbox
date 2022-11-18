# mockserver-example

- https://www.mock-server.com/

```
mvn spring-boot:run
```

```
curl localhost:9080/hello
```

```
curl -v localhost:9080/tasks

curl -v localhost:9080/tasks/1

# ONLY_MATCHING_FIELDSを試す
curl -v localhost:9080/tasks -H "Content-Type: application/json" -d '{"content":"qux"}'
curl -v localhost:9080/tasks -H "Content-Type: application/json" -d '{"content":"quxxx"}'
```

[ダッシュボード](http://localhost:9080/mockserver/dashboard)
