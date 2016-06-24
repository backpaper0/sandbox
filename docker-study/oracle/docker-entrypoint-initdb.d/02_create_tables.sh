#!/bin/bash
for i in /var/init-sql/*.sql;do
  /u01/app/oracle/product/11.2.0/xe/bin/sqlplus "backpaper0/secret" @"$i"
done

