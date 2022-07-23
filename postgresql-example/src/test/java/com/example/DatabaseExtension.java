package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabaseExtension implements BeforeAllCallback {

	// JVMがシャットダウンされるまでコンテナーを維持するためTC_DAEMONをtrueへ設定している
	private final String url = "jdbc:tc:postgresql:14:///example?TC_DAEMON=true";
	private final String user = "example";
	private final String password = "example";

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		Configuration configuration = new FluentConfiguration().dataSource(url, user, password);
		Flyway flyway = new Flyway(configuration);
		flyway.clean();
		flyway.migrate();
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
}
