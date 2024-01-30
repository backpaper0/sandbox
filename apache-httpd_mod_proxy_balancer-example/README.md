# apache-httpd\_mod\_proxy\_balancer-example

`mod\_proxy\_balancer`でロードバランシングするデモ。

- [Apache Module mod\_proxy\_balancer](https://httpd.apache.org/docs/2.4/en/mod/mod_proxy_balancer.html)

## 動作確認

`docker-compose up -d`で起動して`http://localhost:3000`をブラウザで開いて何度かリロード。

## 設定方法メモ

`httpd.conf`を取得する。

```
docker run --rm httpd  cat /usr/local/apache2/conf/httpd.conf > httpd.conf
```

`httpd.conf`を修正する。
ルートパスへのリクエストを`web1`、`web2`へproxyする。
各サーバーへの割り振りはリクエスト毎。

```diff
142c142
< #LoadModule proxy_module modules/mod_proxy.so
---
> LoadModule proxy_module modules/mod_proxy.so
145c145
< #LoadModule proxy_http_module modules/mod_proxy_http.so
---
> LoadModule proxy_http_module modules/mod_proxy_http.so
152c152
< #LoadModule proxy_balancer_module modules/mod_proxy_balancer.so
---
> LoadModule proxy_balancer_module modules/mod_proxy_balancer.so
159c159
< #LoadModule slotmem_shm_module modules/mod_slotmem_shm.so
---
> LoadModule slotmem_shm_module modules/mod_slotmem_shm.so
170c170
< #LoadModule lbmethod_byrequests_module modules/mod_lbmethod_byrequests.so
---
> LoadModule lbmethod_byrequests_module modules/mod_lbmethod_byrequests.so
551a552,557
> <Proxy "balancer://mycluster">
>     BalancerMember "http://web1:80"
>     BalancerMember "http://web2:80"
> </Proxy>
> ProxyPass        "/" "balancer://mycluster"
> ProxyPassReverse "/" "balancer://mycluster"
```

