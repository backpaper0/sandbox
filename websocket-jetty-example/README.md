# websocket-jetty-example

```
mvn -e jetty:run
```

```
$ wscat -c http://localhost:8080/echo
Connected (press CTRL+C to quit)
> hello
< hello
> world
< world
```

