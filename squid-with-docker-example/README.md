# Squid with Docker example

## 概要

SquidをDockerで動かす例。
コンテナイメージは[ubuntu/squid](https://hub.docker.com/r/ubuntu/squid)を使用。

次のような構成で、Squidをforward proxyとして動かす。

![](./diagram.drawio.svg)

## 構築

```bash
docker network create private_net --internal
```

```bash
docker network create public_net
```

```bash
docker run -d --name proxy --network private_net -v "$PWD/squid.conf:/etc/squid/conf.d/_squid.conf:ro" ubuntu/squid
```

```bash
docker network connect public_net proxy
```

## 動作確認

- 接続できる。
  ```bash
  docker run --rm --network private_net curlimages/curl -x http://proxy:3128 https://github.com
  ```

- forward proxyを経由していないので接続できない。
  ```bash
  docker run --rm --network private_net curlimages/curl --connect-timeout 3 https://github.com
  ```

- 許可サイトじゃないので接続できない。
  ```bash
  docker run --rm --network private_net curlimages/curl -x http://proxy:3128 https://zenn.dev/
  ```

- `host.docker.internal`も許可サイトじゃないので接続できない。
  ```bash
  docker run --rm --network private_net curlimages/curl -x http://proxy:3128 http://host.docker.internal:8000
  ```

- もちろんforward proxyを経由しない場合でも`host.docker.internal`へは接続できない。
  ```bash
  docker run --rm --network private_net curlimages/curl --connect-timeout 3 http://host.docker.internal:8000
  ```

## その他

Docker Composeでも動かせられるようにしておいた。

```bash
docker compose up -d
```

```bash
docker compose exec test curl -x http://proxy:3128 https://github.com
```

```bash
docker compose exec test curl --connect-timeout 3 https://github.com
```

```bash
docker compose exec test curl -x http://proxy:3128 https://zenn.dev/
```

```bash
docker compose exec test curl -x http://proxy:3128 http://host.docker.internal:8000
```

```bash
docker compose exec test curl --connect-timeout 3 http://host.docker.internal:8000
```
