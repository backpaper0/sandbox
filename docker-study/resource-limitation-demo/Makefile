.PHONY: build
build:
	mvn package

# 普通に起動
.PHONY: run
run:
	docker run --rm -v $$PWD/target/demo.jar:/demo.jar:ro openjdk:11 java -jar /demo.jar

# CPUを1つに制限
.PHONY: run-cpus1
run-cpus1:
	docker run --rm --cpus 1 -v $$PWD/target/demo.jar:/demo.jar:ro openjdk:11 java -jar /demo.jar

# メモリを128MBに制限
.PHONY: run-memory128m
run-memory128m:
	docker run --rm --memory 128m -v $$PWD/target/demo.jar:/demo.jar:ro openjdk:11 java -jar /demo.jar

# メモリを128MBに制限して-Xmx128mを設定
.PHONY: run-memory128m-xmx128m
run-memory128-xmx128m:
	docker run --rm --memory 128m -v $$PWD/target/demo.jar:/demo.jar:ro openjdk:11 java -Xmx128m -jar /demo.jar

