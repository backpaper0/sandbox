locals {
  # グループ名 => 所属ユーザー
  groups = {
    fruit  = ["apple", "peach", "mandarin"]
    sweets = ["cake", "chocolate"]
  }

  # ユーザー名 => 所属グループ
  users = merge([
    for group, members in local.groups : {
      for user in members : user => group
    }
  ]...)
}

resource "litellm_team" "group" {
  for_each = local.groups

  team_id    = each.key
  team_alias = each.key
}

resource "litellm_user" "user" {
  for_each = local.users

  user_id         = each.key
  user_alias      = each.key
  user_role       = "internal_user"
  teams           = [litellm_team.group[each.value].id]
  auto_create_key = false
}

# litellm_key.key は write-only で state に残らないため、値は Terraform 側で生成して渡す
resource "random_password" "key" {
  for_each = local.users

  length  = 48
  special = false
}

resource "litellm_key" "user" {
  for_each = local.users

  key       = "sk-${random_password.key[each.key].result}"
  key_alias = each.key
  user_id   = litellm_user.user[each.key].id
  team_id   = litellm_team.group[each.value].id

  # write-only の key は変更しても差分にならないので、値が変わったら作り直す
  lifecycle {
    replace_triggered_by = [random_password.key[each.key]]
  }
}

output "keys" {
  value     = { for name, k in litellm_key.user : name => "sk-${random_password.key[name].result}" }
  sensitive = true
}
