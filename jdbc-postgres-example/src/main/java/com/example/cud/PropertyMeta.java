package com.example.cud;

import java.sql.PreparedStatement;

import org.springframework.jdbc.core.SqlParameterValue;

public interface PropertyMeta {

	String getColumnName();

	boolean isAutoIncrement();

	PropertyMeta.Value getValue(Object entity);

	void bindAutoIncrementValue(Object entity, Object value);

	boolean isVersion();

	void bindInitialVersion(PreparedStatement pst, int index);

	void bindInitialVersion(Object entity);

	boolean isPrimaryKey();

	void incrementVersion(Object entity);

	Class<?> getJavaType();

	SqlParameterValue toInitialVersionSqlParameterValue();

	public interface Value {

		boolean isNull();

		void bind(PreparedStatement pst, int index);

		PropertyMeta getPropertyMeta();

		SqlParameterValue toSqlParameterValue();
	}

}
