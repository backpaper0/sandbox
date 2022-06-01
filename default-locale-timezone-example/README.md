
```bash
docker run --rm -v $HOME/.m2:/root/.m2 -v $PWD:/workspace -w /workspace maven:3-eclipse-temurin-17 mvn clean verify
```

