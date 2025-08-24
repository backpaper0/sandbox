#!/bin/bash
set -euo pipefail

cat << EOF > /usr/local/bin/hello
#!/bin/bash
set -euo pipefail

echo "Hello World"
EOF

chmod +x /usr/local/bin/hello