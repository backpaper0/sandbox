package com.example.cud;

import org.springframework.jdbc.core.SqlParameterValue;

public interface PropertyMeta {

	String getColumnName();

	Class<?> getJavaType();

	PropertyMeta.Value getValue(Object entity);

	boolean isPrimaryKey();

	boolean isAutoIncrement();

	boolean isVersion();

	void bindValue(Object entity, Object value);

	void bindInitialVersion(Object entity);

	void bindIncrementVersion(Object entity);

	public interface Value {

		boolean isNull();

		PropertyMeta getPropertyMeta();

		SqlParameterValue toSqlParameterValue();

		Value convertInitialVersionValueIfVersion();
	}

}
