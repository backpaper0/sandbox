create table comics (
	id serial primary key,
	title varchar(100) not null
);

insert into comics (title) values ('2.5次元の誘惑'), ('ココロのプログラム'), ('正反対な君と僕');
