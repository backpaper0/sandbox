package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class UnicodeTest {

	@Test
	void select() throws Exception {
		String sql = "select vc, char_length(vc) from unicode_example order by id";

		// char_length関数はコードポイントを数えるみたい

		try (var con = Connections.get();
				var pst = con.prepareStatement(sql);
				var rs = pst.executeQuery()) {

			rs.next();
			//普通の非漢字
			assertEquals("あ", rs.getString(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(1, "あ".codePointCount(0, "あ".length()));

			rs.next();
			//普通の漢字
			assertEquals("鮭", rs.getString(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(1, "鮭".codePointCount(0, "鮭".length()));

			rs.next();
			//サロゲートペア
			assertEquals("𩸽", rs.getString(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(1, "𩸽".codePointCount(0, "𩸽".length()));

			rs.next();
			//絵文字
			assertEquals("🍣", rs.getString(1));
			assertEquals(1, rs.getInt(2));
			assertEquals(1, "🍣".codePointCount(0, "🍣".length()));

			rs.next();
			//ZWJで結合した絵文字
			assertEquals("👨‍👩‍👧‍👦", rs.getString(1));
			//4(絵文字) + 3(ZWJ) = 7
			assertEquals(7, rs.getInt(2));
			assertEquals(7, "👨‍👩‍👧‍👦".codePointCount(0, "👨‍👩‍👧‍👦".length()));

			rs.next();
			//異体字セレクタ
			assertEquals("朝󠄁", rs.getString(1));
			assertEquals(2, rs.getInt(2));
			assertEquals(2, "朝󠄁".codePointCount(0, "朝󠄁".length()));
		}
	}

	@Test
	void insert() throws Exception {
		String sql = "insert into unicode_example_2 (vc) values (?)";

		//varchar(n)のnはコードポイントの数っぽい

		try (var con = Connections.get();
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
