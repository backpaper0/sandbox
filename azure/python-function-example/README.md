# Azure Functions with Python example

## 参考

- [コマンド ラインから Python 関数を作成する - Azure Functions | Microsoft Learn](https://learn.microsoft.com/ja-jp/azure/azure-functions/create-first-function-cli-python?tabs=macos%2Cbash%2Cazure-cli%2Cbrowser)

## プロジェクトの初期化

```bash
func init --python
```

```bash
poetry init -q --dependency="azure-functions"
```

```bash
rm requirements.txt
```

```bash
poetry install
```

## 関数の追加

```bash
func new --name HttpExample --template "HTTP trigger" --authlevel "anonymous"
```

## ローカル実行

```bash
func start
```

```bash
curl http://localhost:7071/api/HttpExample
```

## Azureへデプロイ

### リソースの準備

```bash
app_name=...
```

```bash
rg_name=${app_name}rg
storage_name=${app_name}storage
loc_name=japaneast
```

```bash
az group create --name $rg_name --location $loc_name
```

```bash
az storage account create --name $storage_name --location $loc_name --resource-group $rg_name --sku Standard_LRS
```

```bash
az functionapp create --resource-group $rg_name --consumption-plan-location $loc_name --runtime python --runtime-version 3.11 --functions-version 4 --name $app_name --os-type linux --storage-account $storage_name
```

### デプロイする

```bash
poetry export -o requirements.txt
```

```bash
func azure functionapp publish $app_name
```

### 動作確認


```bash
func_key=$(az functionapp function keys list --resource-group $rg_name --name $app_name --function-name HttpExample | jq -r ".default")
```


```bash
curl -v -H "x-functions-key: $func_key" https://${app_name}.azurewebsites.net/api/HttpExample
```

```bash
curl -v -H "x-functions-key: $func_key" https://${app_name}.azurewebsites.net/api/HttpExample -G -d name=xxx
```

### リソースの削除

```bash
az group delete --name $rg_name
```
