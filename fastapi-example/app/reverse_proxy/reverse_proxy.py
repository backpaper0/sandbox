"""
リバースプロキシーから渡ってくるX-Forwarded-Forを確認する。
ついでに他のリクエストヘッダーも見てみる。

動作確認手順:
1. poetry run task dev でFastAPIアプリケーションを起動する
2. docker compose up -d でリバースプロキシー(Nginx)を起動する
3. curl localhost:8080/reverse-proxy/ -s | jq を実行する
"""
from fastapi import APIRouter, Request

router = APIRouter()

@router.get("/")
async def get_x_forwarded_for(request: Request):
    ip_addresses = [
        ip_address
        for item in request.headers.get("X-Forwarded-For", "").split(",")
        if (ip_address := item.strip())
    ]
    return {
        "X-Forwarded-For": ip_addresses,
        "headers": dict(request.headers),
    }
