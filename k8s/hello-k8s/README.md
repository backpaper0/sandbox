# hello-k8s

kindでKubernetesクラスターを構築し、Nginxをデプロイする。

## Kubernetesクラスターの構築

kindを使用する。

- https://kind.sigs.k8s.io/

```bash
kind create cluster
```

クラスターの名前はデフォルトでは`kind`となる。
明示的に名前を付けたい場合は`--name=<クラスター名>`オプションを付ける。

```bash
# 現在起動中のクラスターを一覧する
kind get clusters

# クラスターに対応するkubeconfigを確認する
kind get kubeconfig

# クラスターを削除する
kind delete cluster
```

## コマンドでNginxをデプロイする

コマンドを使用してNginxをデプロイする。

```bash
# deploymentを作成する。--replicas=<n>を付けるとpod数を増やせる(デフォルトだと1)
kubectl create deployment nginx --image=nginx

# serviceを作成する。この設定だとポート8080で待ち受けてpodの80へ送信する
kubectl create service clusterip nginx --tcp=8080:80

# 動作確認のためポートフォワードする
kubectl port-forward service/nginx 8080
```

次のURLへアクセスしてNginxが起動していることを確認する。

- http://localhost:8080/

一度、すべて破棄する。

```bash
kubectl delete service/nginx
kubectl delete deployment/nginx
```

## YAMLファイルでNginxをデプロイする

```bash
# serviceとdeploymentを作成する
kubectl apply -f demo.yaml

# 動作確認のためポートフォワードする
kubectl port-forward service/nginx 8080
```

次のURLへアクセスしてNginxが起動していることを確認する。

- http://localhost:8080/

破棄する。

```bash
kubectl delete -f demo.yaml
```

## YAML形式で状態を取得する

```bash
kubectl get service/nginx deployments/nginx -o=yaml
```

### 負荷分散

デプロイ済みのNginxをスケールアウトさせる。

```bash
kubectl scale deployment nginx --replicas 2
```

2つに増えたNginxへラウンドロビンでアクセスしたいが、ポートフォワードは使えない。
なぜなら`kubectl port-forward`はserviceではなくpodへポートフォワードしているため。

ここでは`kubectl proxy`を使用する。

```bash
kubectl proxy
```

次のURLでNginxへアクセスできる。

- http://localhost:8001/api/v1/namespaces/default/services/nginx/proxy/

負荷分散できているかどうかは`kubectl logs -f pods/nginx-<suffix>`でpodごとにログを表示してアクセスログを確認する。
