## 動作確認

Nginxで確認。

まずビルドをする。

```
npm run build
```

Nginxを起動する。

```
docker run -it --rm -p 3000:80 -v $(pwd)/nginx/default.conf:/etc/nginx/conf.d/default.conf -v $(pwd)/build:/app/foobar nginx
```

`http://localhost:3000`にアクセスするとNginxの画面が表示される。
`http://localhost:3000/foobar/`にアクセスするとReactアプリケーションが表示される。

