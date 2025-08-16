#!/bin/bash

echo "Install bar"

cat > /tmp/bar \
<< EOF
#!/bin/sh
echo "Bar $(TZ=Asia/Tokyo date -Iseconds)"
EOF

sudo mv /tmp/bar /usr/local/bin/bar

sudo chmod +x /usr/local/bin/bar
