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
az group create --name AzureFunctionsQuickstart-rg --location japaneast
```

```bash
az storage account create --name $STORAGE_NAME --location japaneast --resource-group AzureFunctionsQuickstart-rg --sku Standard_LRS
```

```bash
az functionapp create --resource-group AzureFunctionsQuickstart-rg --consumption-plan-location westeurope --runtime python --runtime-version 3.11 --functions-version 4 --name $APP_NAME --os-type linux --storage-account $STORAGE_NAME
```

### デプロイする

```bash
poetry export -o requirements.txt
```

```bash
func azure functionapp publish $APP_NAME
```

### 動作確認

```bash
curl -v https://$APP_NAME.azurewebsites.net/api/HttpExample
```

```bash
curl -v https://$APP_NAME.azurewebsites.net/api/HttpExample -G -d name=xxx
```

### リソースの削除

```bash
az group delete --name AzureFunctionsQuickstart-rg
```
