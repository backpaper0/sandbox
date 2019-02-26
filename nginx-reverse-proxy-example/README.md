# nginx-reverse-proxy

```
mvn -f api/pom.xml package
docker-compose up -d
```

```
curl localhost:8080/hello
```

or

```
docker run -it --rm --network nginx-reverse-proxy-example_default busybox sh
wget proxy1/hello -O -
```
