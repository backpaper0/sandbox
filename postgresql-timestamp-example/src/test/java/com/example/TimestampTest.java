package com.example;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.Test;

public class TimestampTest {

	@Test
	public void test() throws Exception {

		final Configuration configuration = new FluentConfiguration()
				.dataSource(TinyDataSource.forFlyway());
		final Flyway flyway = new Flyway(configuration);
		flyway.migrate();

		final UUID id = UUID.fromString("12345678-90ab-cdef-1234-567890abcdef");

		final DataSource dataSource = TinyDataSource.forTest();
		final Connection con = dataSource.getConnection();
		final PreparedStatement pst = con.prepareStatement("select t1, t2 from hoge where id = ?");
		pst.setObject(1, id);
		final ResultSet rs = pst.executeQuery();
		rs.next();
		for (int column = 1, columnCount = rs.getMetaData()
				.getColumnCount(); column <= columnCount; column++) {
			dump(rs, column);
		}
		rs.close();
		pst.close();
		con.close();
	}

	void dump(final ResultSet rs, final int column) throws SQLException {
		p("%s%n", nCopies(100, "*").stream().collect(joining()));
		p("* column = %s%n", column);
		final ResultSetMetaData rsmd = rs.getMetaData();
		p("ResultSetMetaData.getColumnTypeName: %s%n", rsmd.getColumnTypeName(column));

		p("ResultSetMetaData.getColumnType: %s%n", rsmd.getColumnType(column));

		p("ResultSet.getTimestamp: %s%n", rs.getTimestamp(column));

		p("ResultSet.getObject: %s%n", rs.getObject(column));

		p("ResultSet.getObject.getClass: %s%n", rs.getObject(column).getClass());

		p("ResultSet.getString: %s%n", rs.getString(column));

		p("ResultSet.getBytes: %s%n", Arrays.toString(rs.getBytes(column)));
		p("new String(ResultSet.getBytes): %s%n", new String(rs.getBytes(column)));
		p("%s%n", nCopies(100, "*").stream().collect(joining()));
	}

	static void p(final String s, final Object... args) {
		System.err.printf(s, args);
	}

	static class TinyDataSource implements DataSource {

		private final String url;
		private final Properties info;

		private TinyDataSource(final Map<String, String> props) {
			url = "jdbc:postgresql://localhost:5432/example";
			info = new Properties();
			info.setProperty("user", "example");
			info.setProperty("password", "example");
			props.forEach(info::setProperty);
		}

		public static DataSource forFlyway() {
			return new TinyDataSource(Map.of());
		}

		public static DataSource forTest() {
			return new TinyDataSource(Map.of());
			//			return new TinyDataSource(Map.of("loggerLevel", "TRACE"));
		}

		@Override
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T unwrap(final Class<T> iface) throws SQLException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isWrapperFor(final Class<?> iface) throws SQLException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Connection getConnection() throws SQLException {
			return DriverManager.getConnection(url, info);
		}

		@Override
		public Connection getConnection(final String username, final String password)
				throws SQLException {
			return getConnection();
		}

		@Override
		public PrintWriter getLogWriter() throws SQLException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLogWriter(final PrintWriter out) throws SQLException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLoginTimeout(final int seconds) throws SQLException {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getLoginTimeout() throws SQLException {
			throw new UnsupportedOperationException();
		}
	}
}
