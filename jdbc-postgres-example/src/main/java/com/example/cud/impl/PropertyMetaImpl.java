package com.example.cud.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.util.ReflectionUtils;

import com.example.cud.AutoCudException;
import com.example.cud.PropertyMeta;

public class PropertyMetaImpl implements PropertyMeta {

	private final String columnName;
	private final int dataType;
	private final PropertyDescriptor propertyDescriptor;
	private final boolean autoIncrement;
	private final ValueOperator valueOperator;
	private final boolean version;
	private final boolean primaryKey;

	public PropertyMetaImpl(String columnName, int dataType, PropertyDescriptor propertyDescriptor,
			boolean autoIncrement, boolean primaryKey) {
		this.columnName = columnName;
		this.dataType = dataType;
		this.propertyDescriptor = propertyDescriptor;
		this.autoIncrement = autoIncrement;
		this.valueOperator = ValueOperator.valueOf(dataType);
		this.version = columnName.equals("version");
		this.primaryKey = primaryKey;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	@Override
	public PropertyMeta.Value getValue(Object entity) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Object value = ReflectionUtils.invokeMethod(readMethod, entity);
		return new PropertyMetaImpl.ValueImpl(value);
	}

	@Override
	public void bindAutoIncrementValue(Object entity, ResultSet rs) {
		Object value = valueOperator.getAutoIncrementValue(rs);
		Method writeMethod = propertyDescriptor.getWriteMethod();
		ReflectionUtils.invokeMethod(writeMethod, entity, value);
	}

	@Override
	public boolean isVersion() {
		return version;
	}

	@Override
	public void bindInitialVersion(PreparedStatement pst, int index) {
		Object version = valueOperator.getInitialVersion();
		try {
			pst.setObject(index, version, dataType);
		} catch (SQLException e) {
			throw new AutoCudException(e);
		}
	}

	@Override
	public void bindInitialVersion(Object entity) {
		Object version = valueOperator.getInitialVersion();
		Method writeMethod = propertyDescriptor.getWriteMethod();
		ReflectionUtils.invokeMethod(writeMethod, entity, version);
	}

	@Override
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	@Override
	public void incrementVersion(Object entity) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Object value = ReflectionUtils.invokeMethod(readMethod, entity);
		value = valueOperator.incrementVersion(value);
		Method writeMethod = propertyDescriptor.getWriteMethod();
		ReflectionUtils.invokeMethod(writeMethod, entity, value);
	}

	private interface ValueOperator {

		Object getAutoIncrementValue(ResultSet rs);

		Object getInitialVersion();

		Object incrementVersion(Object value);

		static ValueOperator valueOf(int dataType) {
			switch (dataType) {
			case Types.INTEGER:
				return IntegerValueOperator.SINGLETON;
			case Types.BIGINT:
				return LongValueOperator.SINGLETON;
			default:
				return UnsupportedOperationValueOperator.SINGLETON;
			}

		}
	}

	private enum IntegerValueOperator implements ValueOperator {
		SINGLETON;

		@Override
		public Object getAutoIncrementValue(ResultSet rs) {
			try {
				return rs.getInt(1);
			} catch (SQLException e) {
				throw new AutoCudException(e);
			}
		}

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
		public Object getAutoIncrementValue(ResultSet rs) {
			try {
				return rs.getLong(1);
			} catch (SQLException e) {
				throw new AutoCudException(e);
			}
		}

		@Override
		public Object getInitialVersion() {
			return 1L;
		}

		@Override
		public Object incrementVersion(Object value) {
			return ((long) value) + 1L;
		}
	}

	private enum UnsupportedOperationValueOperator implements ValueOperator {
		SINGLETON;

		@Override
		public Object getAutoIncrementValue(ResultSet rs) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getInitialVersion() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object incrementVersion(Object value) {
			throw new UnsupportedOperationException();
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
		public void bind(PreparedStatement pst, int index) {
			try {
				if (isNull()) {
					pst.setNull(index, dataType);
				} else {
					pst.setObject(index, value, dataType);
				}
			} catch (SQLException e) {
				throw new AutoCudException(e);
			}
		}

		@Override
		public PropertyMeta getPropertyMeta() {
			return PropertyMetaImpl.this;
		}
	}
}
