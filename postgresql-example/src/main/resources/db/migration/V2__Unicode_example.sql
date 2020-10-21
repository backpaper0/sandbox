create table unicode_example (
	id int primary key,
	vc varchar(10)
);

insert into unicode_example (id, vc) values (1, '鮭');
insert into unicode_example (id, vc) values (2, '𩸽');
insert into unicode_example (id, vc) values (3, '👨‍👩‍👧‍👦');

create table unicode_example_2 (
	id serial primary key,
	vc varchar(10)
);

