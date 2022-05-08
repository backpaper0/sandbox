package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneratedKey {

	private final Connection con;

	private GeneratedKey(Connection con) {
		this.con = con;
	}

	public static GeneratedKey.Factory factory(String url) {
		return new GeneratedKey.Factory(url);
	}

	public void close() throws SQLException {
		con.close();
	}

	public int insert(String col2) throws SQLException {
		try (PreparedStatement pst = con.prepareStatement("insert into table1 (col2) values (?)")) {
			pst.setString(1, col2);
			return pst.executeUpdate();
		}
	}

	public int key() throws SQLException {
		try (PreparedStatement pst = con.prepareStatement("select currval(pg_get_serial_sequence(?, ?))")) {
			pst.setString(1, "table1");
			pst.setString(2, "col1");
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
				throw new RuntimeException();
			}
		}
	}

	public static class Factory {

		private final String url;

		private Factory(String url) {
			this.url = url;
		}

		@SuppressWarnings("resource")
		public GeneratedKey create() throws SQLException {
			Connection con = DriverManager.getConnection(url);
			return new GeneratedKey(con);
		}
	}
}
