import duckdb

with duckdb.connect() as con:
    con.sql("select * from read_csv('data/fruits.csv')").show()

    con.execute(
        """
        create table fr
        as
        select
            フルーツ名 as name,
            色 as color,
            "平均重量(グラム)" as weight
        from
            read_csv('data/fruits.csv')
        """
    )

    con.sql("select * from fr where weight > 100").show()
