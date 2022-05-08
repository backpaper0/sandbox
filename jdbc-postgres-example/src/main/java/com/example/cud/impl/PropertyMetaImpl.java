package com.example.cud.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Types;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.util.ReflectionUtils;

import com.example.cud.PropertyMeta;

public class PropertyMetaImpl implements PropertyMeta {

	private final String columnName;
	private final int dataType;
	private final PropertyDescriptor propertyDescriptor;
	private final boolean autoIncrement;
	private final boolean version;
	private final boolean primaryKey;

	public PropertyMetaImpl(String columnName, int dataType, PropertyDescriptor propertyDescriptor,
			boolean autoIncrement, boolean primaryKey) {
		this.columnName = columnName;
		this.dataType = dataType;
		this.propertyDescriptor = propertyDescriptor;
		this.autoIncrement = autoIncrement;
		this.version = columnName.equals("version");
		this.primaryKey = primaryKey;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public Class<?> getJavaType() {
		return propertyDescriptor.getPropertyType();
	}

	@Override
	public PropertyMeta.Value getValue(Object entity) {
		Object value = getValueInternal(entity);
		return new PropertyMetaImpl.ValueImpl(value);
	}

	@Override
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	@Override
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	@Override
	public boolean isVersion() {
		return version;
	}

	@Override
	public void bindValue(Object entity, Object value) {
		bindValueInternal(entity, value);
	}

	@Override
	public void bindInitialVersion(Object entity) {
		Object value = valueOperator().getInitialVersion();
		bindValueInternal(entity, value);
	}

	@Override
	public void bindIncrementVersion(Object entity) {
		Object value = getValueInternal(entity);
		value = valueOperator().incrementVersion(value);
		bindValueInternal(entity, value);
	}

	private Object getValueInternal(Object entity) {
		Method readMethod = propertyDescriptor.getReadMethod();
		return ReflectionUtils.invokeMethod(readMethod, entity);
	}

	private void bindValueInternal(Object entity, Object value) {
		Method writeMethod = propertyDescriptor.getWriteMethod();
		ReflectionUtils.invokeMethod(writeMethod, entity, value);
	}

	private ValueOperator valueOperator() {
		return ValueOperator.valueOf(dataType);
	}

	private interface ValueOperator {

		Object getInitialVersion();

		Object incrementVersion(Object value);

		static ValueOperator valueOf(int dataType) {
			switch (dataType) {
			case Types.INTEGER:
				return IntegerValueOperator.SINGLETON;
			case Types.BIGINT:
				return LongValueOperator.SINGLETON;
			default:
				throw new UnsupportedOperationException();
			}

		}
	}

	private enum IntegerValueOperator implements ValueOperator {
		SINGLETON;

		@Override
		public Object getInitialVersion() {
			return 1;
		}

		@Override
		public Object incrementVersion(Object value) {
			return ((int) value) + 1;
		}
	}

	private enum LongValueOperator implements ValueOperator {
		SINGLETON;

		@Override
		public Object getInitialVersion() {
			return 1L;
		}

		@Override
		public Object incrementVersion(Object value) {
			return ((long) value) + 1L;
		}
	}

	private class ValueImpl implements PropertyMeta.Value {

		private final Object value;

		private ValueImpl(Object value) {
			this.value = value;
		}

		@Override
		public boolean isNull() {
			return value == null;
		}

		@Override
		public PropertyMeta getPropertyMeta() {
			return PropertyMetaImpl.this;
		}

		@Override
		public PropertyMeta.Value convertInitialVersionValueIfVersion() {
			if (isVersion()) {
				return new ValueImpl(valueOperator().getInitialVersion());
			}
			return this;
		}

		@Override
		public SqlParameterValue toSqlParameterValue() {
			return new SqlParameterValue(dataType, value);
		}
	}
}
