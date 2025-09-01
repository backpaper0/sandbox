# Nexus example

```bash
docker compose up -d && docker compose logs -f --no-log-prefix nexus-setup
```

```bash
docker compose exec maven mvn compile exec:java
```

```bash
docker compose exec docker docker run --rm hello-world
```

```bash
docker compose exec node npm start
```

```bash
docker compose exec python uv run main.py
```
