create table timestamp_example (
	id uuid primary key,
	t1 timestamp with time zone,
	t2 timestamp
);

-- insert into timestamp_example (id, t1, t2) values ('12345678-90ab-cdef-1234-567890abcdef', current_timestamp, localtimestamp);
insert into timestamp_example (id, t1, t2) values ('12345678-90ab-cdef-1234-567890abcdef', timestamp with time zone'2020-01-02 03:04:05+09', timestamp'2020-01-02 03:04:05');
