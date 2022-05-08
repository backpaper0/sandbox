
-- 主キー自動生成
create table table1 (
	col1 serial primary key,
	col2 int,
	col3 int
);

-- 明示的な主キー
create table table2 (
	col1 int primary key,
	col2 int,
	col3 int
);

-- 複合主キー
create table table3 (
	col1 int,
	col2 int,
	col3 int,
	primary key (col1, col2)
);

-- 主キーなし
create table table4 (
	col1 int,
	col2 int,
	col3 int
);
