# LiteLLM example

```bash
docker compose up -d
```

LiteLLMを経由して[AnthropicのMessages API](https://docs.anthropic.com/en/api/messages)で[Ollama](https://ollama.com/)で動かしている`gpt-oss:20b`へプロンプトを送信してみる。
前提として`ollama serve`でOllamaを起動していること。

```bash
curl localhost:4000/v1/messages -s \
  --json '{"model":"gpt-oss","max_tokens":1024,"messages":[{"role":"user","content":"やあ"}]}' | jq
```

``json
{
  "id": "chatcmpl-5d4602d4-5860-40ef-8e1d-1259bc97b03a",
  "type": "message",
  "role": "assistant",
  "model": "ollama/gpt-oss:20b",
  "stop_sequence": null,
  "usage": {
    "input_tokens": 73,
    "output_tokens": 135
  },
  "content": [
    {
      "type": "text",
      "text": "やあ！こんにちは 😊  \n今日は何かお手伝いできることがありますか？どんな話題でも大丈夫ですよ！"
    }
  ],
  "stop_reason": "end_turn"
}
```

