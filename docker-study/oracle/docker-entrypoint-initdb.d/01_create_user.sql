CREATE USER backpaper0
    IDENTIFIED BY "secret"
    DEFAULT TABLESPACE users
    TEMPORARY TABLESPACE temp
    ;

GRANT DBA TO backpaper0;

