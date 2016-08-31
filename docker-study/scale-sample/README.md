# Load balancer + Web application SAMPLE

## Ready

```
gradlew build
```

## Run

```
docker-compose up -d
```

```
curl dockerhost
#curl `docker-machine ip dev`
```

## Scale

```
docker-compose scale web=2
```

