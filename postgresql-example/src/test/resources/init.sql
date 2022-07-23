create table timestamp_example (
	id uuid primary key,
	t1 timestamp with time zone,
	t2 timestamp
);

-- insert into timestamp_example (id, t1, t2) values ('12345678-90ab-cdef-1234-567890abcdef', current_timestamp, localtimestamp);
insert into timestamp_example (id, t1, t2) values ('12345678-90ab-cdef-1234-567890abcdef', timestamp with time zone'2020-01-02 03:04:05+09', timestamp'2020-01-02 03:04:05');



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



create table readonly_example (
	id int primary key,
	val varchar(100)
);
