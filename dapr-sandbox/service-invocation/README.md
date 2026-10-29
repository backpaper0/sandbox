# Service Invocationを試す

- https://docs.dapr.io/developing-applications/building-blocks/service-invocation/

## ツールの準備

```
mise install
```

## Dockerで試す

Daprを準備する。

```
dapr init
```

依存ライブラリをインストールする。

```
mise run npm:ci
```

起動する。

```
dapr run -f .
```

`curl`で試す。

```
curl localhost:$(dapr list -o json | jq -r 'map(select(.appId == "hello-svc")) | .[].httpPort')/v1.0/invoke/hello-svc/method/hello -s | jq
```

動的に設定されたDaprのポートを`dapr list`と`jq`で取得しているけれど、`dapr run`に`-H`でポートを明示的に設定することも可能。

### 後始末

アプリケーションは`ctrl + c`で止める。

Daprをアンインストールする。

```
dapr uninstall
```

または

```
dapr uninstall --all
```

## Kubernetes(Kind)で試す

Kindでクラスターを作ってDaprを初期化する。

```
kind create cluster
```

```
kubectl cluster-info --context kind-kind
```

```
dapr init -k
```

`hello`と`world`のコンテナイメージを作る。

```
mise run pack:build
```

Kindにコンテナイメージをロードする。

```
kind load docker-image demo/hello demo/world
```

デプロイする。

```
kubectl apply -f demo.yaml
```

動作状況を確認する。

```
kubectl get -f demo.yaml -o wide
kubectl get pods -o wide
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
