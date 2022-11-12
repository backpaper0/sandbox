# Ingress

Ingressを試す。

- https://kubernetes.io/ja/docs/concepts/services-networking/ingress/
- https://kubernetes.io/ja/docs/concepts/services-networking/ingress-controllers/

数あるIngress Controllerの中から、kindのドキュメントにチュートリアルがあるContourと、KubernetesがサポートしているIngress NGINX Controllerのふたつを試してみる。

## クラスター構築

クラスター構築方法はいずれのIngress Controllerでも手順は同じ。

```bash
kind create cluster --config config.yaml
```

## Contour

- https://projectcontour.io/
- https://kind.sigs.k8s.io/docs/user/ingress/

```bash
# デプロイ
kubectl apply -f https://projectcontour.io/quickstart/contour.yaml
```

```bash
# パッチを当てる。
kubectl patch daemonsets -n projectcontour envoy -p '{"spec":{"template":{"spec":{"nodeSelector":{"ingress-ready":"true"},"tolerations":[{"key":"node-role.kubernetes.io/control-plane","operator":"Equal","effect":"NoSchedule"},{"key":"node-role.kubernetes.io/master","operator":"Equal","effect":"NoSchedule"}]}}}}'
```

[動作確認](#動作確認)を行う。

## Ingress NGINX Controller

- https://github.com/kubernetes/ingress-nginx
- https://kubernetes.github.io/ingress-nginx/deploy/

```bash
# デプロイ
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.5.1/deploy/static/provider/kind/deploy.yaml
```

[動作確認](#動作確認)を行う。

## 動作確認

kindが提供しているチュートリアルにある`usage.yaml`を利用させてもらって動作確認を行う。

```bash
# デプロイ
kubectl apply -f https://kind.sigs.k8s.io/examples/ingress/usage.yaml
```

```bash
curl localhost:8080/foo
```

```bash
curl localhost:8080/bar
```

動作確認が終わったらクラスターを破棄する。

```bash
kind delete cluster
```

