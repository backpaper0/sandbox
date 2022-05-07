package com.example;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrimaryKeys {

	private final List<PrimaryKey> primaryKeys;

	private PrimaryKeys(List<PrimaryKey> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public static PrimaryKeys create(Connection con, String tableName) throws SQLException {
		List<PrimaryKey> pks = new ArrayList<>();
		DatabaseMetaData databaseMetaData = con.getMetaData();
		Set<String> autoIncrementColumnNames = new HashSet<>();
		try (ResultSet columns = databaseMetaData.getColumns(null, null, tableName, null)) {
			while (columns.next()) {
				if (columns.getString("IS_AUTOINCREMENT").equals("YES")) {
					autoIncrementColumnNames.add(columns.getString("COLUMN_NAME"));
				}
			}
		}
		try (ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName)) {
			while (primaryKeys.next()) {
				PrimaryKey primaryKey = PrimaryKey.create(primaryKeys, autoIncrementColumnNames);
				pks.add(primaryKey);
			}
		}
		Collections.sort(pks);
		pks = Collections.unmodifiableList(pks);
		return new PrimaryKeys(pks);
	}

	public List<PrimaryKey> getPrimaryKeys() {
		return primaryKeys;
	}

	public static class PrimaryKey implements Comparable<PrimaryKey> {

		private final String columnName;
		private final short keySeq;
		private final boolean autoIncrement;

		private PrimaryKey(String columnName, short keySeq, boolean autoIncrement) {
			this.columnName = columnName;
			this.keySeq = keySeq;
			this.autoIncrement = autoIncrement;
		}

		private static PrimaryKey create(ResultSet resultSet, Set<String> autoIncrementColumnNames)
				throws SQLException {
			String columnName = resultSet.getString("COLUMN_NAME");
			short keySeq = resultSet.getShort("KEY_SEQ");
			boolean autoIncrement = autoIncrementColumnNames.contains(columnName);
			return new PrimaryKey(columnName, keySeq, autoIncrement);
		}

		@Override
		public int compareTo(PrimaryKey o) {
			return Short.compare(keySeq, o.keySeq);
		}

		public String getColumnName() {
			return columnName;
		}

		public boolean isAutoIncrement() {
			return autoIncrement;
		}
	}
}
