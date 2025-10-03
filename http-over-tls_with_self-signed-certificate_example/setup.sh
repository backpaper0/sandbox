#!/bin/bash
set -euo pipefail

openssl genrsa -out nginx/tls/ca.key 4096

openssl req -x509 -new -nodes -days 3650 -key nginx/tls/ca.key -sha256 -subj "/CN=My Local Root CA" -out nginx/tls/ca.crt

openssl genrsa -out nginx/tls/server.key 2048

openssl req -new -key nginx/tls/server.key -out nginx/tls/server.csr -config san.cnf

openssl x509 -req -in nginx/tls/server.csr -CA nginx/tls/ca.crt -CAkey nginx/tls/ca.key -CAcreateserial -out nginx/tls/server.crt -days 825 -sha256 -extfile san.cnf -extensions req_ext
