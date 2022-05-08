package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class GeneratedKeysTest {

	static GeneratedKey sut1, sut2;

	@BeforeAll
	static void init() throws Exception {
		GeneratedKey.Factory factory = GeneratedKey
				.factory("jdbc:tc:postgresql:14.2:///example?TC_INITSCRIPT=init-for-generatedkeys.sql");
		sut1 = factory.create();
		sut2 = factory.create();
	}

	@AfterAll
	static void close() throws SQLException {
		if (sut1 != null) {
			sut1.close();
		}
		if (sut2 != null) {
			sut2.close();
		}
	}

	@Test
	void test() throws SQLException {

		assertEquals(1, sut1.insert("foo"));
		int pk1 = sut1.key();

		assertEquals(1, sut2.insert("bar"));
		int pk2 = sut2.key();

		// serial列の現在値はコネクション毎にキャッシュされているっぽい
		assertEquals(pk1, sut1.key());

		// インクリメントするときはコネクション毎のキャッシュは無視（当たり前っちゃ当たり前）
		sut1.insert("baz");
		assertTrue(pk2 < sut1.key());
	}
}
