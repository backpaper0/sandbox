create table unicode_example (
	id int primary key,
	vc varchar(10)
);

insert into unicode_example (id, vc) values (1, 'あ');
insert into unicode_example (id, vc) values (2, '鮭');
insert into unicode_example (id, vc) values (3, '𩸽');
insert into unicode_example (id, vc) values (4, '🍣');
insert into unicode_example (id, vc) values (5, '👨‍👩‍👧‍👦');
insert into unicode_example (id, vc) values (6, '朝󠄁');

create table unicode_example_2 (
	id serial primary key,
	vc varchar(10)
);

