package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connections {

	// JVMがシャットダウンされるまでコンテナーを維持するためTC_DAEMONをtrueへ設定している
	private static final String url = "jdbc:tc:postgresql:14:///example?TC_DAEMON=true&TC_INITSCRIPT=init.sql";
	private static final String user = "example";
	private static final String password = "example";

	public static Connection get() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
}
