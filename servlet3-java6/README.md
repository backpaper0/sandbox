
```
docker run --rm -it -v `pwd`:/workspace -v ~/.m2:/root/.m2 maven:3-jdk-6 mvn -f /workspace/pom.xml clean integration-test
```

