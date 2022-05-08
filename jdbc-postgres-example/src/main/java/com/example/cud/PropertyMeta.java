package com.example.cud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface PropertyMeta {

	String getColumnName();

	boolean isAutoIncrement();

	PropertyMeta.Value getValue(Object entity);

	void bindAutoIncrementValue(Object entity, ResultSet rs);

	boolean isVersion();

	void bindInitialVersion(PreparedStatement pst, int index);

	void bindInitialVersion(Object entity);

	boolean isPrimaryKey();

	void incrementVersion(Object entity);

	public interface Value {

		boolean isNull();

		void bind(PreparedStatement pst, int index);

		PropertyMeta getPropertyMeta();
	}

}
