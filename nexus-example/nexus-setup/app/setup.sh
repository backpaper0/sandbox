#!/bin/sh
set -eu

NEXUS_URL=http://nexus:8081/service/rest/v1
NEXUS_TIMEOUT=${NEXUS_TIMEOUT:-60}

ADMIN_USER="admin"
ADMIN_PASSWORD="pass1234"

DEPLOY_USER="deploy"
DEPLOY_USER_FIRSTNAME="Deploy"
DEPLOY_USER_LASTNAME="User"
DEPLOY_USER_EMAIL="deploy@example.org"
DEPLOY_USER_PASSWORD="pass1234"
DEPLOY_ROLE="deploy"

BLOB_STORE="default"

DOCKER_HOSTED_NAME=docker-hosted
DOCKER_PROXY_NAME=docker-proxy
DOCKER_GROUP_NAME=docker-group
DOCKER_HOSTED_PORT=5001
DOCKER_PROXY_PORT=5002
DOCKER_GROUP_PORT=5000

NPM_HOSTED_NAME=npm-hosted
NPM_PROXY_NAME=npm-proxy
NPM_GROUP_NAME=npm-group

PYPI_HOSTED_NAME=pypi-hosted
PYPI_PROXY_NAME=pypi-proxy
PYPI_GROUP_NAME=pypi-group

do_get() {
    local request_path="$1"
    curl -Gs -u "${ADMIN_USER}:${ADMIN_PASSWORD}" "${NEXUS_URL}${request_path}"
}

do_get_status_code() {
    local request_path="$1"
    local response=$(curl -Gs -w "%{http_code}" -u "${ADMIN_USER}:${ADMIN_PASSWORD}" "${NEXUS_URL}${request_path}")
    local status_code="${response: -3}"
    echo "${status_code}"
}

send_request() {
    local request_path="$1"
    local request_body="$2"
    local http_method="${3:-POST}"
    local response=$(curl -X "${http_method}" -sS -o /dev/stderr -w "%{http_code}" -u "${ADMIN_USER}:${ADMIN_PASSWORD}" "${NEXUS_URL}${request_path}" -H "Content-Type: application/json" -d "${request_body}")
    local status_code="${response: -3}"
    if [[ "${status_code}" -lt "200" || "${status_code}" -gt "299" ]] ; then
        exit 1
    fi
}

wait_nexus() {
    basis=$(date +%s)
    echo "Nexusの起動を確認します"
    while ! curl -s ${NEXUS_URL}/status/writable; do
        sleep 1
        now=$(date +%s)
        if [ "$((now - basis))" -ge "${NEXUS_TIMEOUT}" ]; then
            echo -e "Nexusの起動が確認できませんでした（タイムアウト）"
            exit 1
        fi
        echo "."
    done
    echo "Nexusの起動を確認しました"
}

change_admin_password() {
    if [ ! -f /nexus-data/admin.password ]; then
        return 0
    fi
    echo "${ADMIN_USER}のパスワードを変更します"
    if ! curl -sS -o /dev/stderr -u ${ADMIN_USER}:$(cat /nexus-data/admin.password) \
        -X PUT ${NEXUS_URL}/security/users/${ADMIN_USER}/change-password \
        -H "Content-Type: text/plain" -d "${ADMIN_PASSWORD}"
    then
        echo -e "${ADMIN_USER}のパスワードを変更できませんでした"
        exit 1
    fi
    echo "${ADMIN_USER}のパスワードを変更しました"
}

enable_anonymous_access() {
    if [ "$(do_get /security/anonymous | jq '.enabled')" == "true" ]; then
        return 0
    fi
    echo "匿名アクセスを有効にします"
    local request_body=$(cat << EOF
{
    "enabled": true,
    "userId": "anonymous",
    "realmName": "NexusAuthorizingRealm"
}
EOF
    )
    send_request /security/anonymous "${request_body}" PUT
    echo "匿名アクセスを有効にしました"
}

accept_community_edition_eula() {
    local response=$(do_get /system/eula)
    local accepted=$(jq -n --argjson response "${response}" -r '$response.accepted')
    if [ "${accepted}" == "true" ]; then
        return 0
    fi
    echo "使用許諾を受け入れます"
    local request_body=$(jq -n --argjson response "${response}" -r '$response + {"accepted":true}')
    send_request /system/eula "${request_body}"
    echo "使用許諾を受け入れました"
}

create_deploy_role() {
    local status_code=$(do_get_status_code "/security/roles/${DEPLOY_ROLE}")
    if [ "${status_code}" == "200" ]; then
        return 0
    fi
    echo "デプロイ用のロールを作成します"
    local request_body=$(cat << EOF
{
    "id": "${DEPLOY_ROLE}",
    "name": "${DEPLOY_ROLE}",
    "description": "${DEPLOY_ROLE}",
    "privileges": [ "nx-repository-view-*-*-*" ],
    "roles": []
}
EOF
    )
    send_request /security/roles "${request_body}"
    echo "デプロイ用のロールを作成しました"
}

create_deploy_user() {
    local exists_deploy_user=$(do_get /security/users | jq '[.[] | select(.userId == "deploy")] | any')
    if [ "${exists_deploy_user}" == "true" ]; then
        return 0
    fi
    echo "デプロイ用のユーザーを作成します"
    local request_body=$(cat << EOF
{
    "userId": "${DEPLOY_USER}",
    "firstName": "${DEPLOY_USER_FIRSTNAME}",
    "lastName": "${DEPLOY_USER_LASTNAME}",
    "emailAddress": "${DEPLOY_USER_EMAIL}",
    "password": "${DEPLOY_USER_PASSWORD}",
    "status": "active",
    "roles": [ "${DEPLOY_ROLE}" ]
}
EOF
    )
    send_request /security/users "${request_body}"
    echo "デプロイ用のユーザーを作成しました"
}

activate_realms() {
    local docker_token_realm="DockerToken"
    local npm_token_realm="NpmToken"
    local realms=$(do_get /security/realms/active)
    local before_length=$(jq -n --argjson realms "${realms}" '$realms | length')
    if [ "$(jq -n --argjson realms "${realms}" --arg realm "${docker_token_realm}" '$realms | any(. == $realm)')" == "false" ]; then
        realms=$(jq -n --argjson realms "${realms}" --arg realm "${docker_token_realm}" '$realms + [$realm]')
    fi
    if [ "$(jq -n --argjson realms "${realms}" --arg realm "${npm_token_realm}" '$realms | any(. == $realm)')" == "false" ]; then
        realms=$(jq -n --argjson realms "${realms}" --arg realm "${npm_token_realm}" '$realms + [$realm]')
    fi
    local after_length=$(jq -n --argjson realms "${realms}" '$realms | length')
    if [ "${before_length}" == "${after_length}" ]; then
        return 0
    fi
    echo "必要なレルムを有効化します"
    local request_body='["NexusAuthenticatingRealm", "DockerToken", "NpmToken"]'
    send_request /security/realms/active "${request_body}" PUT
    echo "必要なレルムを有効化しました"
}

################################################################################
# Docker

create_docker_hosted() {
    if [ "$(do_get_status_code "/repositories/docker/hosted/${DOCKER_HOSTED_NAME}")" == "200" ]; then
        return 0
    fi
    echo "Dockerのhostedリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${DOCKER_HOSTED_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true,
        "writePolicy": "allow"
    },
    "docker": {
        "v1Enabled": false,
        "forceBasicAuth": true,
        "httpPort": ${DOCKER_HOSTED_PORT}
    }
}
EOF
    )
    send_request /repositories/docker/hosted "${request_body}"
    echo "Dockerのhostedリポジトリを作成しました"
}

create_docker_proxy() {
    if [ "$(do_get_status_code "/repositories/docker/proxy/${DOCKER_PROXY_NAME}")" == "200" ]; then
        return 0
    fi
    echo "Dockerのproxyリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${DOCKER_PROXY_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true
    },
    "proxy": {
        "remoteUrl": "https://registry-1.docker.io",
        "contentMaxAge": 1440,
        "metadataMaxAge": 1440
    },
    "negativeCache": {
        "enabled": true,
        "timeToLive": 1440
    },
    "httpClient": {
        "blocked": false,
        "autoBlock": true
    },
    "docker": {
        "v1Enabled": false,
        "forceBasicAuth": true,
        "httpPort": ${DOCKER_PROXY_PORT}
    },
    "dockerProxy": {
        "indexType": "HUB"
    }   
}
EOF
    )
    send_request /repositories/docker/proxy "${request_body}"
    echo "Dockerのproxyリポジトリを作成しました"
}

create_docker_group() {
    if [ "$(do_get_status_code "/repositories/docker/group/${DOCKER_GROUP_NAME}")" == "200" ]; then
        return 0
    fi
    echo "Dockerのgroupリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${DOCKER_GROUP_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true
    },
    "group": {
        "memberNames": ["${DOCKER_HOSTED_NAME}", "${DOCKER_PROXY_NAME}"]
    },
    "docker": {
        "v1Enabled": false,
        "forceBasicAuth": false,
        "httpPort": ${DOCKER_GROUP_PORT}
    }
}
EOF
    )
    send_request /repositories/docker/group "${request_body}"
    echo "Dockerのgroupリポジトリを作成しました"
}

################################################################################
# NPM

create_npm_hosted() {
    if [ "$(do_get_status_code "/repositories/npm/hosted/${NPM_HOSTED_NAME}")" == "200" ]; then
        return 0
    fi
    echo "NPMのhostedリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${NPM_HOSTED_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true,
        "writePolicy": "allow"
    }
}
EOF
    )
    send_request /repositories/npm/hosted "${request_body}"
    echo "NPMのhostedリポジトリを作成しました"
}

create_npm_proxy() {
    if [ "$(do_get_status_code "/repositories/npm/proxy/${NPM_PROXY_NAME}")" == "200" ]; then
        return 0
    fi
    echo "NPMのproxyリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${NPM_PROXY_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true
    },
    "proxy": {
        "remoteUrl": "https://registry.npmjs.org/",
        "contentMaxAge": 1440,
        "metadataMaxAge": 1440
    },
    "negativeCache": {
        "enabled": true,
        "timeToLive": 1440
    },
    "httpClient": {
        "blocked": false,
        "autoBlock": true
    },
    "npm": {
        "removeQuarantined": true
    }
}

EOF
    )
    send_request /repositories/npm/proxy "${request_body}"
    echo "NPMのproxyリポジトリを作成しました"
}

create_npm_group() {
    if [ "$(do_get_status_code "/repositories/npm/group/${NPM_GROUP_NAME}")" == "200" ]; then
        return 0
    fi
    echo "NPMのgroupリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${NPM_GROUP_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true
    },
    "group": {
        "memberNames": [ "${NPM_HOSTED_NAME}", "${NPM_PROXY_NAME}" ]
    }
}
EOF
    )
    send_request /repositories/npm/group "${request_body}"
    echo "NPMのgroupリポジトリを作成しました"
}

################################################################################
# PyPI

create_pypi_hosted() {
    if [ "$(do_get_status_code "/repositories/pypi/hosted/${PYPI_HOSTED_NAME}")" == "200" ]; then
        return 0
    fi
    echo "PyPIのhostedリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${PYPI_HOSTED_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true,
        "writePolicy": "allow"
    }
}
EOF
    )
    send_request /repositories/pypi/hosted "${request_body}"
    echo "PyPIのhostedリポジトリを作成しました"
}

create_pypi_proxy() {
    if [ "$(do_get_status_code "/repositories/pypi/proxy/${PYPI_PROXY_NAME}")" == "200" ]; then
        return 0
    fi
    echo "PyPIのproxyリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${PYPI_PROXY_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true
    },
    "proxy": {
        "remoteUrl": "https://pypi.org/",
        "contentMaxAge": 1440,
        "metadataMaxAge": 1440
    },
    "negativeCache": {
        "enabled": true,
        "timeToLive": 1440
    },
    "httpClient": {
        "blocked": false,
        "autoBlock": true
    },
    "pypi": {
        "removeQuarantined": true
    }
}
EOF
    )
    send_request /repositories/pypi/proxy "${request_body}"
    echo "PyPIのproxyリポジトリを作成しました"
}

create_pypi_group() {
    if [ "$(do_get_status_code "/repositories/pypi/group/${PYPI_GROUP_NAME}")" == "200" ]; then
        return 0
    fi
    echo "PyPIのgroupリポジトリを作成します"
    local request_body=$(cat << EOF
{
    "name": "${PYPI_GROUP_NAME}",
    "online": true,
    "storage": {
        "blobStoreName": "${BLOB_STORE}",
        "strictContentTypeValidation": true
    },
    "group": {
        "memberNames": [ "${PYPI_HOSTED_NAME}", "${PYPI_PROXY_NAME}" ]
    }
}
EOF
    )
    send_request /repositories/pypi/group "${request_body}"
    echo "PyPIのgroupリポジトリを作成しました"
}

################################################################################
# main

echo "Nexusのセットアップを開始します"

wait_nexus

change_admin_password
enable_anonymous_access
accept_community_edition_eula
create_deploy_role
create_deploy_user
activate_realms

create_docker_hosted
create_docker_proxy
create_docker_group

create_npm_hosted
create_npm_proxy
create_npm_group

create_pypi_hosted
create_pypi_proxy
create_pypi_group

echo "Nexusのセットアップが完了しました"
