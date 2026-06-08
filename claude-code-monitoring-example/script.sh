export OTEL_EXPORTER_OTLP_HEADERS="Authorization=Basic $(echo -n "root@example.com:Complexpass#123" | base64),stream-name=claude-code"

