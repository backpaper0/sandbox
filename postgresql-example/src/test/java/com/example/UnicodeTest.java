package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class UnicodeTest {

	@RegisterExtension
	static DatabaseExtension databaseExtension = new DatabaseExtension();

	@Test
	void select() throws Exception {
		String sql = "select vc, char_length(vc) from unicode_example where id between 1 and 3 order by id";

		// char_length関数はコードポイントを数えるみたい

		try (var con = databaseExtension.getConnection();
				var pst = con.prepareStatement(sql);
				var rs = pst.executeQuery()) {

			rs.next();
			assertEquals("鮭", rs.getString(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(1, "鮭".codePointCount(0, "鮭".length()));

			rs.next();
			assertEquals("𩸽", rs.getString(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(1, "𩸽".codePointCount(0, "𩸽".length()));

			rs.next();
			assertEquals("👨‍👩‍👧‍👦", rs.getString(1));
			//4(絵文字) + 3(ZWJ) = 7
			assertEquals(7, rs.getInt(2));
			assertEquals(7, "👨‍👩‍👧‍👦".codePointCount(0, "👨‍👩‍👧‍👦".length()));

		}
	}

	@Test
	void insert() throws Exception {
		String sql = "insert into unicode_example_2 (vc) values (?)";

		//varchar(n)のnはコードポイントの数っぽい

		try (var con = databaseExtension.getConnection();
				var pst1 = con.prepareStatement("delete from unicode_example_2");
				var pst2 = con.prepareStatement(sql)) {
			//テーブルをクリアする
			pst1.executeUpdate();

			pst2.setString(1, "𩸽𩸽𩸽𩸽𩸽𩸽𩸽𩸽𩸽𩸽");
			assertEquals(1, pst2.executeUpdate());

			pst2.setString(1, "𩸽𩸽𩸽𩸽𩸽𩸽𩸽𩸽𩸽𩸽x");
			assertThrows(SQLException.class, () -> pst2.executeUpdate());

			pst2.setString(1, "👨‍👩‍👧‍👦123");
			assertEquals(1, pst2.executeUpdate());

			pst2.setString(1, "👨‍👩‍👧‍👦1234");
			assertThrows(SQLException.class, () -> pst2.executeUpdate());
		}
	}
}
