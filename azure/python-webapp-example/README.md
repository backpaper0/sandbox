# App Service

## ローカル開発

```bash
curl localhost:8000/chat/invoke -H "Content-Type: application/json" -d '{"input":"PythonでHello Worldして。"}' -s | jq
```

```json
{
  "output": "もちろんです！Pythonで「Hello, World!」を表示する簡単なコードは以下の通りです。\n\n```python\nprint(\"Hello, World!\")\n```\n\nこのコードを実行すると、コンソールに「Hello, World!」と表示されます。Pythonの実行環境でこのコードを試してみてください！",
  "metadata": {
    "run_id": "61c024f2-fcdd-4f68-9dce-c9b3c07bebe4",
    "feedback_tokens": []
  }
}
```

## デプロイ

https://learn.microsoft.com/ja-jp/azure/app-service/deploy-zip?tabs=cli

## メモ

- リソースグループ
- 仮想ネットワーク
    - サブネット
- App Service
- Azure OpenAI
- Cosmos DB
