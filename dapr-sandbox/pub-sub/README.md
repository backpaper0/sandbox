# Publish & subscribeを試す

- https://docs.dapr.io/developing-applications/building-blocks/pubsub/
- https://docs.dapr.io/reference/api/pubsub_api/

## Dockerで試す

Daprを準備する。

```
dapr init
```

`subscription.yaml`のシンボリックリンクを作成する（ファイルをコピーしても良い）。

```
ln -s $PWD/components/subscription.yaml ~/.dapr/components/
```

`subapp`を起動する。

```
cd subapp
```

```
npm ci
```

```
dapr run -a subapp -p 3001 npm start
```

`dapr publish`で試す。

```
dapr publish -i subapp -p pubsub -t demo -d '{"message":"Hello, Subscription!"}'
```

`pubapp`も試す。

```
cd pubapp
```

```
npm ci
```

```
dapr run -a pubapp -p 3000 npm start
```

`curl`で試す。

```
curl localhost:3000/publish -d message='Hello, Pub-Sub!'
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

Pub/Subメッセージブローカーを作る。

- 参考：https://docs.dapr.io/getting-started/configure-state-pubsub/#create-pubsub-message-broker-component

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
kubectl apply -f redis-pubsub.yaml
```

`pubapp`と`subapp`のコンテナイメージを作る。

```
ls | grep app | xargs -t -I{} pack build demo/{} --path {}
```

Kindにコンテナイメージをロードする。

```
ls | grep app | awk '{print "demo/" $0}' | xargs kind load docker-image
```

デプロイする。

```
kubectl apply -f components/subscription.yaml -f demo.yaml
```

ポートフォワードする。

```
kubectl port-forward svc/pubapp-service 3000:
```

`subapp`のログを表示しておく。

```
kubectl logs -f deploy/subapp-deploy subapp-container
```

`curl`で動作確認する。

```
curl localhost:3000/publish -d message='Hello, Pub-Sub in Kubernetes!'
```

### 後始末

リソースを削除する。

```
kubectl delete -f components/subscription.yaml -f demo.yaml -f redis-pubsub.yaml
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
