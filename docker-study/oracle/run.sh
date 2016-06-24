#!/bin/sh
docker run -d \
    -p 1521:1521 -p 1022:22 \
    -v `pwd`/docker-entrypoint-initdb.d:/docker-entrypoint-initdb.d \
    -v `pwd`/init-sql:/var/init-sql \
    --name=oracle wnameless/oracle-xe-11g

