# Azure Container Appsを試す

## 参考

- [Azure Container Apps で最初のコンテナ化された Azure Functions を作成する | Microsoft Learn](https://learn.microsoft.com/ja-jp/azure/azure-functions/functions-deploy-container-apps?tabs=acr%2Cbash&pivots=programming-language-python)
- まずレジストリを準備しておく必要がある
    - [クイックスタート - ポータルでのレジストリを作成する - Azure Container Registry | Microsoft Learn](https://learn.microsoft.com/ja-jp/azure/container-registry/container-registry-get-started-portal?tabs=azure-cli#create-a-container-registry)
- `az functionapp create`の`--workload-profile-name`へ渡す値が説明されている
    - [Workload profiles in Azure Container Apps | Microsoft Learn](https://learn.microsoft.com/en-us/azure/container-apps/workload-profiles-overview)

## プロジェクト初期化

```bash
poetry init -q
```

```bash
func init --worker-runtime python --docker
```

## 起動・停止など

### ローカル（非コンテナ）

```bash
func start
```

動作確認。

```bash
curl localhost:7071/api/HttpExample
```

```bash
curl localhost:7071/api/HttpExample -G -d name=MyName
```

### ローカル（コンテナ）

#### ビルド

```bash
docker build --tag myfun .
```

#### 動作確認

```bash
docker run -p 8080:80 -it --rm myfun
```

```bash
curl localhost:8080/api/HttpExample
```

```bash
curl localhost:8080/api/HttpExample -G -d name=MyName
```

### Azure

#### 環境構築・デプロイ

変数を設定。

```bash
resource_basename=...
```

```bash
location=japaneast
resource_group=${resource_basename}rg
container_registry=${resource_basename}cr
storage_account=${resource_basename}sa
func_name=${resource_basename}fn
func_env=${resource_basename}env
```

リソースグループを作成する。

```bash
az group create --name $resource_group --location $location
```

コンテナレジストリを作成する。

```bash
az acr create \
  --name $container_registry --resource-group $resource_group \
  --sku "Basic" --admin-enabled true --location $location
```

コンテナイメージをデプロイする。

```bash
docker tag myfun ${container_registry}.azurecr.io/myfun:v1.0.0
```

```bash
az acr login --name $container_registry
```

```bash
docker push ${container_registry}.azurecr.io/myfun:v1.0.0
```

ストレージアカウントを作成する。

```bash
az storage account create \
  --name $storage_account --resource-group $resource_group \
  --location $location --sku "Standard_LRS"
```

Azure Container App環境を作成する。

```bash
az containerapp env create \
  --name $func_env --resource-group $resource_group --location $location
```

関数アプリを作成する。

```bash
registry_username=$(az acr credential show --name $container_registry \
  --resource-group $resource_group | jq -r ".username")
```

```bash
registry_password=$(az acr credential show --name $container_registry \
  --resource-group $resource_group | jq -r ".passwords[0].value")
```

```bash
az functionapp create \
  --name $func_name --resource-group $resource_group \
  --storage-account $storage_account --environment $func_env \
  --functions-version 4 --runtime python \
  --image ${container_registry}.azurecr.io/myfun:v1.0.0 \
  --workload-profile-name Consumption --assign-identity \
  --registry-server ${container_registry}.azurecr.io \
  --registry-username $registry_username \
  --registry-password $registry_password
```

#### 動作確認

```bash
func_host=$(az functionapp show --name $func_name \
  --resource-group $resource_group | jq -r ".defaultHostName")
```

```bash
curl https://${func_host}/api/HttpExample
```

```bash
curl https://${func_host}/api/HttpExample -G -d name=MyName
```

#### 後始末

```bash
az group delete --name $resource_group
```
