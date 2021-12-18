# State Managementを試す

- https://docs.dapr.io/developing-applications/building-blocks/state-management/
- https://docs.dapr.io/reference/api/state_api/

## Dockerで試す

Daprを準備する。

```
dapr init
```

`counter`を起動する。

```
cd counter
```

```
npm ci
```

```
dapr run -a counter -p 3000 npm start
```

`curl`で試す。

```
curl localhost:3000/count -s | jq
```

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

Redis Storeを作る。

- 参考：https://docs.dapr.io/getting-started/configure-state-pubsub/#create-a-redis-store

```
helm repo add bitnami https://charts.bitnami.com/bitnami
```

```
helm repo update
```

```
helm install redis bitnami/redis
```

```
kubectl apply -f redis-state.yaml
```

`counter`のコンテナイメージを作る。

```
pack build demo/counter --path counter
```

Kindにコンテナイメージをロードする。

```
kind load docker-image demo/counter
```

デプロイする。

```
kubectl apply -f demo.yaml
```

ポートフォワードする。

```
kubectl port-forward svc/counter-service 3000:
```

`curl`で動作確認する。

```
curl localhost:3000/count -s | jq
```

### 後始末

リソースを削除する。

```
kubectl delete -f demo.yaml
```

```
kubectl delete -f redis-state.yaml
```

```
helm uninstall redis
```

Daprをアンインストールする。

```
dapr uninstall -k
```

クラスターを削除する。

```
kind delete cluster
```
