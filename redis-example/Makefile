.PHONY: build
build:
	mvn package -DskipTests
	mvn dependency:copy-dependencies -DincludeScope=runtime

.PHONY: up
up:
	docker compose up -d

.PHONY: down
down:
	docker compose down

.PHONY: cli
cli:
	docker compose exec redis redis-cli

.PHONY: test1
test1:
	mvn -B compile exec:java -Dexec.mainClass=com.example.RedisExample

.PHONY: test2
test2:
#	mvn -B compile exec:java -Dexec.mainClass=com.example.DelExpiredEventDemo
	java -cp "target/redis-example-1.0-SNAPSHOT.jar:target/dependency/*" com.example.DelExpiredEventDemo

