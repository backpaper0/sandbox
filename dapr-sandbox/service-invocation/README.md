# Service Invocationを試す

- https://docs.dapr.io/developing-applications/building-blocks/service-invocation/

## Dockerで試す

Daprを準備する。

```
dapr init
```

`hello`を起動する。

```
cd hello
```

```
npm ci
```

```
dapr run -a hello-svc -p 3000 npm start
```

`world`を起動する。

```
cd world
```

```
npm ci
```

```
dapr run -a world-svc -p 3001 npm start
```

`curl`で試す。

```
curl localhost:$(dapr list -o json | jq -r '.[0].httpPort')/v1.0/invoke/hello-svc/method/hello -s | jq
```

動的に設定されたDaprのポートを`dapr list`と`jq`で取得しているけれど、`dapr run`に`-H`でポートを明示的に設定することも可能。

### 後始末

アプリケーションは`ctrl + c`で止める。

Daprをアンインストールする。

```
dapr uninstall --all
```

## Kubernetes(Kind)で試す

Kindでクラスターを作ってDaprを初期化する。

```
kind create cluster
```

```
kind export kubeconfig
```

```
dapr init -k
```

`hello`と`world`それぞれコンテナイメージを作る。

```
pack build demo/hello --path hello
```

```
pack build demo/world --path world
```

Kindにコンテナイメージをロードする。

```
kind load docker-image demo/hello demo/world
```

デプロイする。

```
kubectl apply -f demo.yaml
```

ポートフォワードする。

```
kubectl port-forward svc/hello-service 3000:
```

`curl`で動作確認する。

```
curl localhost:3000/hello -s | jq
```

### 後始末

リソースを削除する。

```
kubectl delete -f demo.yaml
```

Daprをアンインストールする。

```
dapr uninstall -k
```

クラスターを削除する。

```
kind delete cluster
```

