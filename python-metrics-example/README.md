# Python Metrics Example

PrometheusとOTLPを使ったPythonメトリクスのサンプルです。

## セットアップ

### Prometheusの起動

OTLPレシーバーを有効にしてPrometheusをDockerで起動します。

```sh
docker run -d --name prom -p 9090:9090 prom/prometheus \
  --config.file=/etc/prometheus/prometheus.yml \
  --storage.tsdb.path=/prometheus \
  --web.enable-otlp-receiver
```

### 依存パッケージのインストール

```sh
uv run sync
```

### アプリケーションの実行

```sh
uv run main.py
```

1秒ごとに以下の3種類のメトリクスをランダム値で生成し、OTLPエクスポーター経由でPrometheusへ送信します（5秒ごとにエクスポート）。

| メトリクス | 種類 | 内容 |
|---|---|---|
| `request_count` | Counter | `/api/hello` へのリクエスト数（累計） |
| `active_connections` | Gauge | アクティブな接続数（5〜20のランダム値） |
| `response_time` | Histogram | レスポンスタイム（10〜500msのランダム値） |

停止するには `Ctrl+C` を押してください。

## Prometheusクエリ

### request_count（Counter）

[リクエスト数の累計](http://localhost:9090/graph?g0.expr=request_count_total&g0.tab=0):

```
request_count_total
```

[1分間のリクエストレート](http://localhost:9090/graph?g0.expr=rate(request_count_total%5B1m%5D)&g0.tab=0):

```
rate(request_count_total[1m])
```

### active_connections（Gauge）

[アクティブな接続数](http://localhost:9090/graph?g0.expr=active_connections_ratio&g0.tab=0):

```
active_connections_ratio
```

### response_time（Histogram）

[p99レイテンシ（1分間）](http://localhost:9090/graph?g0.expr=histogram_quantile(0.99%2C%20rate(response_time_milliseconds_bucket%5B1m%5D))&g0.tab=0):

```
histogram_quantile(0.99, rate(response_time_milliseconds_bucket[1m]))
```

[平均レイテンシ（1分間）](http://localhost:9090/graph?g0.expr=rate(response_time_milliseconds_sum%5B1m%5D)%20%2F%20rate(response_time_milliseconds_count%5B1m%5D)&g0.tab=0):

```
rate(response_time_milliseconds_sum[1m]) / rate(response_time_milliseconds_count[1m])
```

### すべてを一括で見る

[すべてを一括で見る](http://localhost:9090/graph?g0.expr=request_count_total&g0.tab=0&g1.expr=rate(request_count_total%5B1m%5D)&g1.tab=0&g2.expr=active_connections_ratio&g2.tab=0&g3.expr=histogram_quantile(0.99%2C%20rate(response_time_milliseconds_bucket%5B1m%5D))&g3.tab=0&g4.expr=rate(response_time_milliseconds_sum%5B1m%5D)%20%2F%20rate(response_time_milliseconds_count%5B1m%5D)&g4.tab=0)
