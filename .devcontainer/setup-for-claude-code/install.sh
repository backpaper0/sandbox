#!/bin/bash
set -euo pipefail

if [ "$(id -u)" -ne 0 ]; then
    echo -e 'Script must be run as root. Use sudo, su, or add "USER root" to your Dockerfile before running this script.'
    exit 1
fi

mkdir -p ${_REMOTE_USER_HOME}/.claude
chown -R ${_REMOTE_USER}:${_REMOTE_USER} ${_REMOTE_USER_HOME}/.claude

mkdir /commandhistory
touch /commandhistory/.bash_history
chown -R ${_REMOTE_USER}:${_REMOTE_USER} /commandhistory

# common-utilsによって無制限のsudoが許可されているので、取り消す
# https://github.com/devcontainers/features/blob/849a5e2a7fd2c8109531cec9e63bfde12472407a/src/common-utils/main.sh#L440-L445
if [ -e /etc/sudoers.d/${_REMOTE_USER} ]; then
    rm /etc/sudoers.d/${_REMOTE_USER}
fi


# Firewallの設定

apt-get update && apt-get install -y --no-install-recommends \
    iptables \
    ipset \
    iproute2 \
    dnsutils

apt-get clean

rm -rf /var/lib/apt/lists/*

cp "$(dirname "$0")/init-firewall.sh" /usr/local/bin/init-firewall.sh

chmod +x /usr/local/bin/init-firewall.sh

echo "${_REMOTE_USER} ALL=(root) NOPASSWD: /usr/local/bin/init-firewall.sh" > /etc/sudoers.d/${_REMOTE_USER}-firewall

chmod 0440 /etc/sudoers.d/${_REMOTE_USER}-firewall
