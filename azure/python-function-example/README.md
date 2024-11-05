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