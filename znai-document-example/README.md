# znaiを試す

- https://github.com/testingisdocumenting/znai
- https://testingisdocumenting.org/znai

## プレビュー

```sh
mvn znai:preview
```

http://localhost:3333/preview/

## SSG

まずビルドする。

```sh
mvn znai:build
```

簡易にHTTPサーバーを立てて確認してみる。

```sh
npx serve target
```

http://localhost:3000/example/

