```
docker run --rm -it -v `pwd`:/workspace openjdk:14 javac --enable-preview -source 14 -d /workspace /workspace/App.java
docker run --rm -it -v `pwd`:/workspace openjdk:14 java --enable-preview -cp /workspace -XX:+ShowCodeDetailsInExceptionMessages App
```

