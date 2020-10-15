# localhost-https-example

ローカルホストでSPAとAPIをCORSで繋いでHTTPSする例。

## 準備

```
cd spa
npm ci
```

```
cd api
npm ci
```

```
mkcert -key-file nginx/spa-key.pem -cert-file nginx/spa-cert.pem "spa.example.org"
mkcert -key-file nginx/api-key.pem -cert-file nginx/api-cert.pem "api.example.com"
```

## 起動

```
cd spa
npm start
```

```
cd api
npm start
```

```
docker-compose up
```

## 参考資料

- https://nginx.org/en/docs/http/ngx_http_proxy_module.html
- https://nginx.org/en/docs/http/websocket.html
- https://nginx.org/en/docs/http/ngx_http_ssl_module.html

