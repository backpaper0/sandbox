package com.example.cud.impl;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.BeanUtils;

import com.example.cud.EntityMeta;
import com.example.cud.EntityMetaFactory;
import com.example.cud.NamingConvention;
import com.example.cud.PropertyMeta;

public class EntityMetaFactoryImpl implements EntityMetaFactory {

	private final DataSource dataSource;
	private final NamingConvention namingConvention;

	public EntityMetaFactoryImpl(DataSource dataSource, NamingConvention namingConvention) {
		this.dataSource = dataSource;
		this.namingConvention = namingConvention;
	}

	@Override
	public EntityMeta create(Object entity) {
		Class<?> clazz = entity.getClass();
		String tableName = namingConvention.convertToTableName(clazz.getSimpleName());
		Map<String, PropertyDescriptor> propertyDescriptors = new HashMap<>();
		for (PropertyDescriptor propertyDescriptor : BeanUtils.getPropertyDescriptors(clazz)) {
			String columnName = namingConvention.convertToColumnName(propertyDescriptor.getName());
			propertyDescriptors.put(columnName, propertyDescriptor);
		}
		List<PropertyMeta> propertyMetas = new ArrayList<>();
		try (Connection con = dataSource.getConnection()) {
			DatabaseMetaData databaseMetaData = con.getMetaData();

			List<String> primaryKeyColumnNames = new ArrayList<>();
			try (ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName)) {
				while (primaryKeys.next()) {
					primaryKeyColumnNames.add(primaryKeys.getString("COLUMN_NAME"));
				}
			}

			try (ResultSet columns = databaseMetaData.getColumns(null, null, tableName, null)) {
				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					int dataType = columns.getInt("DATA_TYPE");
					PropertyDescriptor propertyDescriptor = propertyDescriptors.get(columnName);
					boolean autoIncrement = columns.getString("IS_AUTOINCREMENT").equals("YES");
					boolean primaryKey = primaryKeyColumnNames.contains(columnName);
					PropertyMeta propertyMeta = new PropertyMetaImpl(columnName, dataType, propertyDescriptor,
							autoIncrement, primaryKey);
					propertyMetas.add(propertyMeta);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return new EntityMetaImpl(tableName, propertyMetas);
	}
}
