#!/bin/bash

echo "Install foo"

cat > /usr/local/bin/foo \
<< EOF
#!/bin/sh
echo "Foo $(TZ=Asia/Tokyo date -Iseconds)"
EOF

chmod +x /usr/local/bin/foo
