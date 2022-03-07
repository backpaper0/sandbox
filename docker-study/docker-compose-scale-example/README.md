```bash
cd app
```

```bash
pack build app
```

```bash
cd ..
```

```bash
docker compose up -d --scale app1=2 --scale app2=2
```

```bash
curl -s localhost:8080/app1 | jq
```

```bash
curl -s localhost:8080/app2 | jq
```

