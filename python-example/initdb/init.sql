create table messages (
    id serial primary key,
    content text not null
);

insert into messages (content) values ('foo'), ('bar'), ('baz'), ('qux');
