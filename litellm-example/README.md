# LiteLLM example

LiteLLM を Docker Compose で起動し、[LiteLLM の Terraform Provider](https://registry.terraform.io/providers/BerriAI/litellm/latest/docs) でチーム（グループ）・ユーザー・仮想キーを管理する。

## 構成

| ファイル | 内容 |
| --- | --- |
| `compose.yaml` | LiteLLM と Postgres |
| `mise.toml` | Terraform のインストールと、Provider 用の環境変数（`LITELLM_API_BASE` / `LITELLM_API_KEY`） |
| `mise-tasks/generate-secret` | `LITELLM_MASTER_KEY` と `LITELLM_SALT_KEY` を `.env` に生成する |
| `versions.tf` | Provider の設定 |
| `main.tf` | チーム・ユーザー・仮想キーの定義 |

## 起動

```bash
mise run generate-secret
docker compose up -d
```

`.env` は一度作ったら作り直さないこと。`LITELLM_SALT_KEY` が変わると、DB に保存済みのクレデンシャルが読めなくなる。

## Terraform

Provider は `mise.toml` が設定する環境変数で LiteLLM に接続する。マスターキーで認証する。

```bash
terraform init
terraform plan
terraform apply
```

### 管理しているリソース

`main.tf` の `local.groups` が定義の起点になっている。

| チーム | ユーザー |
| --- | --- |
| fruit | apple, peach, mandarin |
| sweets | cake, chocolate |

- チーム（`litellm_team`）は `team_id` にチーム名をそのまま使う。
- ユーザー（`litellm_user`）は `user_id` にユーザー名をそのまま使う。所属チームは `teams` で指定する。
- 仮想キー（`litellm_key`）はユーザーごとに 1 本発行する。所属チームに紐付ける。

メンバーを増やしたり減らしたりするときは `local.groups` を編集する。

### 仮想キーの取り出し

```bash
terraform output -json keys
```

`litellm_key` の `key` 属性は write-only なので state に残らない。LiteLLM 側もハッシュしか持っていない。そこでキーの値は `random_password` で生成し、それを渡している。

- キーの値は `random_password` 経由で **tfstate に平文で入る**。tfstate は `.gitignore` 済み。
- 特定のキーだけ作り直すときは次のようにする。`replace_triggered_by` によって、対応する `litellm_key` も作り直される。

  ```bash
  terraform apply -replace='random_password.key["apple"]'
  ```

### 動作確認

```bash
curl -s localhost:4000/key/info -H "Authorization: Bearer $(terraform output -json keys | jq -r .apple)" | jq
```
