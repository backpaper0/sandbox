#!/bin/bash
set -euo pipefail

if [ "$(id -u)" -ne 0 ]; then
    echo -e 'Script must be run as root. Use sudo, su, or add "USER root" to your Dockerfile before running this script.'
    exit 1
fi

cp "$(dirname "$0")/update-workspace-owner.sh" /usr/local/bin/update-workspace-owner.sh

chmod +x /usr/local/bin/update-workspace-owner.sh

echo "${_REMOTE_USER} ALL=(root) NOPASSWD: /usr/local/bin/update-workspace-owner.sh" > /etc/sudoers.d/${_REMOTE_USER}-workspace-owner

chmod 0440 /etc/sudoers.d/${_REMOTE_USER}-workspace-owner