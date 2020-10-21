package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class TimestampTest {

	@RegisterExtension
	static DatabaseExtension databaseExtension = new DatabaseExtension();

	@Test
	public void timestamp() throws Exception {
		UUID id = UUID.fromString("12345678-90ab-cdef-1234-567890abcdef");

		Connection con = databaseExtension.getConnection();
		PreparedStatement pst = con
				.prepareStatement("select t1, t2 from timestamp_example where id = ?");
		pst.setObject(1, id);
		ResultSet rs = pst.executeQuery();
		rs.next();

		ResultSetMetaData md = rs.getMetaData();

		{
			int column = 1;
			assertEquals("timestamptz", md.getColumnTypeName(column));
			//Types.TIMESTAMP_WITH_TIMEZONE ではないみたい
			assertEquals(Types.TIMESTAMP, md.getColumnType(column));
			assertEquals(Timestamp.valueOf("2020-01-02 03:04:05"), rs.getTimestamp(column));
			assertEquals(Timestamp.valueOf("2020-01-02 03:04:05"), rs.getObject(column));
			assertEquals(Timestamp.class, rs.getObject(column).getClass());
			assertEquals("2020-01-02 03:04:05+09", rs.getString(column));
			assertEquals("2020-01-02 03:04:05+09", new String(rs.getBytes(column)));
		}

		{
			int column = 2;
			assertEquals("timestamp", md.getColumnTypeName(column));
			assertEquals(Types.TIMESTAMP, md.getColumnType(column));
			assertEquals(Timestamp.valueOf("2020-01-02 03:04:05"), rs.getTimestamp(column));
			assertEquals(Timestamp.valueOf("2020-01-02 03:04:05"), rs.getObject(column));
			assertEquals(Timestamp.class, rs.getObject(column).getClass());
			assertEquals("2020-01-02 03:04:05", rs.getString(column));
			assertEquals("2020-01-02 03:04:05", new String(rs.getBytes(column)));
		}

		rs.close();
		pst.close();
		con.close();
	}
}
