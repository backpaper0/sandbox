create table queue (
    id int primary key,
    processed boolean not null default false
);

create table gate (
    id int primary key
);

insert into queue (id) select generate_series(1, 100000);

insert into gate (id) values (1);
