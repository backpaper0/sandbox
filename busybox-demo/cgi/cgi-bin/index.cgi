#!/bin/sh

# レスポンスヘッダーまたは空行から開始する必要がある
printf "\r\n"
printf "<h1>Hello, $(whoami)!</h1>"
# 設定された環境変数を見たい場合は次の行のコメントを外す
# env
