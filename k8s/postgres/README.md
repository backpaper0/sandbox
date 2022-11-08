# PostgreSQLを使用する例

PGOを使用する。

- https://access.crunchydata.com/documentation/postgres-operator/v5/quickstart/

まずkindでクラスターを作成する。
なお、これを書いている時点での最新版であるKubernetes 1.25だと`batch/v1beta1`から`beta1`が取れているようでPGO(`postgres-operator-examples`のコミットハッシュは`206cc6aa0aa148133d3147d286f9b3c68b820ffb`)をデプロイできなかったためKubernetes 1.24を使用している。

```bash
kind create cluster --config config.yaml
```

PGOをデプロイする。

```bash
git clone git@github.com:CrunchyData/postgres-operator-examples.git

cd postgres-operator-examples

# もし動かなくなっていたら動作確認済みのコミットをチェックアウトすると良いかも
# git checkout 206cc6aa0aa148133d3147d286f9b3c68b820ffb

kubectl apply -k kustomize/install/namespace

kubectl apply --server-side -k kustomize/install/default
```

PostgreSQLをデプロイする。

```bash
kubectl apply -k kustomize/postgres
```

アプリケーションをデプロイする。

```bash
# コンテナイメージをビルド
./mvnw spring-boot:build-image -DskipTests

# クラスターにコンテナイメージを取り込む
kind load docker-image k8s-postgres-demo:0.0.1-SNAPSHOT

# アプリケーションをデプロイ
kubectl apply -f app.yaml
```

クラスターの外から`curl`で動作確認したいためポートフォワードする。

```bash
kubectl -n postgres-operator port-forward services/demo 8080
```

`curl`で動作確認する。

```bash
curl -s localhost:8080 | jq
```

## その他の情報

`psql`で繋ぐ。

```bash
kubectl -n postgres-operator run -it --rm --image postgres demo-db-cli psql $(kubectl get secrets -n postgres-operator hippo-pguser-hippo -o go-template='{{.data.uri | base64decode}}')
```

後始末。

```bash
kind delete cluster
```

