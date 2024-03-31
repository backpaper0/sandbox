create table messages (
    id int primary key,
    text_content text not null
);

insert into messages (id, text_content) values (1, 'Hello, world!');
