# Logstashで遊ぶ

## 試し方

```
logstash -f <confファイル>
```

## 各confファイルの簡単な説明

* `echo.conf` - 標準入力をそのまま標準出力に返す設定
* `gitlog.conf` - `git log`を受け取る設定(**未完成**)
* `echo-elasticseach.conf` - 標準入力をElasticsearchに渡す設定

## その他

[Logstashのリファレンス](https://www.elastic.co/guide/en/logstash/current/index.html)

