# Hello World

```sh
docker run -d --name mq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

```sh
mvn -P consumer exec:java
```

```sh
mvn -P producer exec:java
# mvn -P producer exec:java -Dexec.arguments=10
```

## Clustering

```sh
docker run -d -h usaq1 --name mq1 -e RABBITMQ_ERLANG_COOKIE=secret -p 5672:5672 -p 15672:15672 rabbitmq:3-management

docker run -d -h usaq2 --name mq2 -e RABBITMQ_ERLANG_COOKIE=secret -p 5673:5672 --link mq1 rabbitmq:3
docker exec -it mq2 bash

rabbitmq-plugins enable rabbitmq_management
# rabbitmq-server -detached
# rabbitmqctl cluster_status
rabbitmqctl stop_app
# rabbitmqctl reset
rabbitmqctl join_cluster rabbit@usaq1
rabbitmqctl start_app
# rabbitmqctl cluster_status
rabbitmqctl set_policy ha-two "^sample" '{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'
```
