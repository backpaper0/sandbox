# DaprのJavaScript SDKを試す

<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

<!-- code_chunk_output -->

- [DaprのJavaScript SDKを試す](#daprのjavascript-sdkを試す)
  - [Dockerで試す](#dockerで試す)
    - [Daprの準備（Docker）](#daprの準備docker)
    - [アプリケーションの起動](#アプリケーションの起動)
    - [Service Invocation, State Management, Pub/Subを試す](#service-invocation-state-management-pubsubを試す)
    - [Actorを試す](#actorを試す)
    - [後始末（Docker）](#後始末docker)

<!-- /code_chunk_output -->

## Dockerで試す

### Daprの準備（Docker）

```
dapr init
```

コンポーネント設定YAMLのシンボリックリンクを作成する（ファイルをコピーしても良い）。

```
ls components | xargs -t -I{} ln -s $PWD/components/{} ~/.dapr/components/
```

### アプリケーションの起動

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

### Service Invocation, State Management, Pub/Subを試す

`calc-app`の`calc`を呼ぶと`add-app`の`add`呼び出しとメッセージのpublishを行う。

メッセージは`add-app`が受け取り、計算結果をState Storeへ保存する。

保存された計算結果は`calc-app`の`result`で取得できる。

これらを試すコマンドの例は次の通り。

```
curl localhost:3000/calc -H "Content-Type: application/json" -d '{"arg1":2,"arg2":3}'
```

計算結果が返ってくる。

また、次の`curl`コマンドでPub/Subによって非同期処理されてState Storeへ保存された計算結果を取得できる。

```
curl localhost:3000/result
```

なお、イベントのsubscribeを動作確認したい場合は`dapr publish`が使える。

```
dapr publish -p pubsub -t add -i add-app -d '{"arg1":3,"arg2":4}'
```

### Actorを試す

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

先程までとはカウント値が異なることから`actorId`毎に`CounterActor`のインスタンスがあることがわかる。

### 後始末（Docker）

アプリケーションは`ctrl + c`で止める。

Daprをアンインストールする。

```
dapr uninstall --all
```

