create table if not exists hoge (
    foo bigint primary key,
    created_at timestamp not null
);

create table if not exists fuga (
    bar bigint primary key,
    baz varchar(100) not null,
    created_at timestamp not null
);

