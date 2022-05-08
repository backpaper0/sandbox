
create table example_table (
	id serial primary key,
	example_col1 varchar(100) default '0',
	example_col2 char(10) default '0',
	example_col3 integer default 0,
	example_col4 bigint default 0,
	example_col5 decimal(10, 2) default 0,
	example_col6 boolean default false,
	example_col7 timestamp default timestamp '2022/01/01 00:00:00',
	example_col8 date default date '2022/01/01',
	example_col9 time default '00:00',
	example_col10 uuid default '99999999-9999-9999-9999-999999999999',
	version integer
);

create table example_table1 (
	id serial primary key,
	example_col1 varchar(100) default 'a',
	example_col2 varchar(100) default 'b',
	version integer
);

create table example_table2 (
	id integer primary key,
	example_col1 varchar(100) default 'a',
	example_col2 varchar(100) default 'b',
	version integer
);

create table example_table3 (
	id1 integer,
	id2 varchar(100),
	example_col1 varchar(100) default 'a',
	example_col2 varchar(100) default 'b',
	version integer,
	primary key (id1, id2)
);

create table example_table4 (
	id serial primary key,
	example_col1 varchar(100) default 'a',
	example_col2 varchar(100) default 'b'
);

create table example_table5 (
	example_col1 varchar(100) default 'a',
	example_col2 varchar(100) default 'b'
);
