#!/bin/bash
set -euo pipefail

if [ "$(id -u)" -ne 0 ]; then
    echo -e 'Script must be run as root. Use sudo, su, or add "USER root" to your Dockerfile before running this script.'
    exit 1
fi

if [ -z "${USERNAME}"]; then
    echo -e "USERNAME environment variable must be set"
    exit 1
fi

cp "$(dirname "$0")/update-workspace-owner.sh" /usr/local/bin/update-workspace-owner.sh

chmod +x /usr/local/bin/update-workspace-owner.sh

echo "${USERNAME} ALL=(root) NOPASSWD: /usr/local/bin/update-workspace-owner.sh" > /etc/sudoers.d/${USERNAME}-workspace-owner

chmod 0440 /etc/sudoers.d/${USERNAME}-workspace-owner
