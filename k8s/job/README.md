# Job、CronJobを試す

- https://kubernetes.io/docs/reference/kubernetes-api/workload-resources/job-v1/
- https://kubernetes.io/docs/reference/kubernetes-api/workload-resources/cron-job-v1/

## 準備

```bash
# クラスターの構築
kind create cluster
```

## Job

[hello-worldコンテナ](https://hub.docker.com/_/hello-world)を動かす。

```bash
# ジョブの実行
kubectl apply -f job.yaml
```

```bash
# ジョブの状態を確認する
kubectl describe jobs hello
```

```bash
# ログを確認する
kubectl logs jobs/hello
```

## CronJob

busyboxで`date`コマンドを実行する。

```bash
# cronジョブの登録
kubectl apply -f cronjob.yaml
```

```bash
# ジョブの状態を確認する
kubectl describe cronjobs/date
```

```bash
# ログを確認する
kubectl logs jobs/date-<suffix>
```

## 後始末

```bash
kind delete cluster
```

