# DaprのJavaScript SDKを試す

## Dockerで試す

Daprを準備する。

```
dapr init
```

コンポーネント設定YAMLのシンボリックリンクを作成する（ファイルをコピーしても良い）。

```
ln -s $PWD/components/subscription.yaml ~/.dapr/components/
```

```
ln -s $PWD/components/share-statestore.yaml ~/.dapr/components/
```

`calc-app`を起動する。

```
cd calc-app
```

```
npm ci
```

```
npm run dapr
```

`add-app`を起動する。

```
cd add-app
```

```
npm ci
```

```
npm run dapr
```

`dapr publish`でPub-Subを試す。

```
dapr publish -p pubsub -t add -i add-app -d '{"arg1":2,"arg2":3}'
```

`curl`でService InvocationとPub-SubとState Managementを試す。

```
curl localhost:3000/calc -H "Content-Type: application/json" -d '{"arg1":2,"arg2":3}'
```

Pub-Subで非同期処理した計算結果はState Storeへ保存される。
次の`curl`コマンドで保存された計算結果を取得できる。

```
curl localhost:3000/result
```

Actorを試す。
単に0から始まってカウントアップするだけのActorを用意した。
`add-app/index.js`に`CounterActor`という名前でクラスを作ってある。

次の`curl`コマンドで試せる。

```
curl localhost:3000/counter -H "Content-Type: application/json" -d '{"actorId":"foo"}' 
```

`actorId`を変えてみる。

```
curl localhost:3000/counter -H "Content-Type: application/json" -d '{"actorId":"bar"}' 
```

`actorId`毎に`CounterActor`のインスタンスがあることがわかる。

### 後始末

アプリケーションは`ctrl + c`で止める。

Daprをアンインストールする。

```
dapr uninstall --all
```

