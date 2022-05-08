package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.PrimaryKeys.PrimaryKey;

@Testcontainers
public class PrimaryKeysTest {

	static Connection con;

	@BeforeAll
	static void init() throws Exception {
		con = DriverManager.getConnection("jdbc:tc:postgresql:14.2:///example?TC_INITSCRIPT=init-for-primarykeys.sql");
	}

	@AfterAll
	static void close() throws SQLException {
		if (con != null) {
			con.close();
		}
	}

	@Test
	void serial() throws SQLException {
		PrimaryKeys sut = PrimaryKeys.create(con, "table1");
		Iterator<PrimaryKey> primaryKeys = sut.getPrimaryKeys().iterator();

		assertTrue(primaryKeys.hasNext());
		PrimaryKey primaryKey = primaryKeys.next();
		assertEquals("col1", primaryKey.getColumnName());
		assertEquals(true, primaryKey.isAutoIncrement());

		assertFalse(primaryKeys.hasNext());
	}

	@Test
	void standard() throws SQLException {
		PrimaryKeys sut = PrimaryKeys.create(con, "table2");
		Iterator<PrimaryKey> primaryKeys = sut.getPrimaryKeys().iterator();

		assertTrue(primaryKeys.hasNext());
		PrimaryKey primaryKey = primaryKeys.next();
		assertEquals("col1", primaryKey.getColumnName());
		assertEquals(false, primaryKey.isAutoIncrement());

		assertFalse(primaryKeys.hasNext());
	}

	@Test
	void composite() throws SQLException {
		PrimaryKeys sut = PrimaryKeys.create(con, "table3");
		Iterator<PrimaryKey> primaryKeys = sut.getPrimaryKeys().iterator();

		assertTrue(primaryKeys.hasNext());
		PrimaryKey primaryKey = primaryKeys.next();
		assertEquals("col1", primaryKey.getColumnName());
		assertEquals(false, primaryKey.isAutoIncrement());

		assertTrue(primaryKeys.hasNext());
		primaryKey = primaryKeys.next();
		assertEquals("col2", primaryKey.getColumnName());
		assertEquals(false, primaryKey.isAutoIncrement());

		assertFalse(primaryKeys.hasNext());
	}

	@Test
	void noPK() throws SQLException {
		PrimaryKeys sut = PrimaryKeys.create(con, "table4");
		Iterator<PrimaryKey> primaryKeys = sut.getPrimaryKeys().iterator();

		assertFalse(primaryKeys.hasNext());
	}
}
