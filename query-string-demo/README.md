# HttpServletRequest.getQueryString()を試す

URLエンコードされたままの値が取得できた。

```sh
% curl -s localhost:8080\?message=$(node -e 'console.log(encodeURI("こんにちは世界"))') | jq
{
  "query": "message=%E3%81%93%E3%82%93%E3%81%AB%E3%81%A1%E3%81%AF%E4%B8%96%E7%95%8C",
  "message": "こんにちは世界"
}
```

