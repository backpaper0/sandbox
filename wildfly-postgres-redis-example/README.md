```
docker compose up -d

docker compose exec wildfly wildfly/bin/add-user.sh admin secret --silent
```

```
docker compose exec wildfly bash

curl -LO https://repo.maven.apache.org/maven2/org/postgresql/postgresql/42.2.23/postgresql-42.2.23.jar

wildfly/bin/jboss.cli --connect

deploy postgresql-42.2.23.jar --name=postgresql

/subsystem=datasources/data-source=example:add(driver-name=postgresql, jndi-name=java:/example, driver-class=org.postgresql.Driver, connection-url=jdbc:postgresql://db.example.com:5432/exampledb, user-name=exampleuser, password=examplepass)
```

```
docker compose exec postgres psql -U exampleuser exampledb

create table tags (id serial primary key, name varchar(100) not null, version bigint not null default(0));

insert into tags (name) values ('Java'), ('WildFly'), ('PostgreSQL'), ('Redis');
```

```
mvn package

docker compose cp target/wildfly-postgres-redis-example-app-0.1-SNAPSHOT.war wildfly:opt/jboss/wildfly-postgres-redis-example-app-0.1-SNAPSHOT.war

docker compose exec wildfly wildfly/bin/jboss-cli.sh --connect --command="deploy wildfly-postgres-redis-example-app-0.1-SNAPSHOT.war --name=example.war --force"
```

```
curl -v localhost:8080/example/tags

curl -v localhost:8080/example/tags -d name=example

curl -v localhost:8080/example/cache/greeting -d value=HelloWorld

curl -v localhost:8080/example/cache/greeting
```

```
docker compose exec redis redis-cli

get greeting
```


