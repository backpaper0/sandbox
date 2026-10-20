terraform {
  required_providers {
    litellm = {
      source  = "BerriAI/litellm"
      version = "~> 1.106"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.7"
    }
  }
}

# 接続先と認証は mise.toml で設定している環境変数 LITELLM_API_BASE / LITELLM_API_KEY から読む
provider "litellm" {}
