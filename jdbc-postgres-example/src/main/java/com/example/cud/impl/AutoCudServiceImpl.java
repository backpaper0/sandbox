package com.example.cud.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.SqlParameterValue;

import com.example.cud.AutoCudException;
import com.example.cud.AutoCudService;
import com.example.cud.EntityMeta;
import com.example.cud.EntityMetaFactory;
import com.example.cud.PropertyMeta;

public class AutoCudServiceImpl implements AutoCudService {

	private final JdbcOperations jdbcOperations;
	private final EntityMetaFactory entityMetaFactory;

	public AutoCudServiceImpl(JdbcOperations jdbcOperations, EntityMetaFactory entityMetaFactory) {
		this.jdbcOperations = jdbcOperations;
		this.entityMetaFactory = entityMetaFactory;
	}

	@Override
	public int insert(Object entity) {
		return insert(entity, false);
	}

	@Override
	public int insertExcludesNull(Object entity) {
		return insert(entity, true);
	}

	private int insert(Object entity, boolean excludesNull) {
		EntityMeta entityMeta = entityMetaFactory.create(entity);

		StringBuilder query1 = new StringBuilder();
		StringBuilder query2 = new StringBuilder();

		query1.append("insert into ").append(entityMeta.getTableName()).append("(");
		query2.append(") values (");

		List<PropertyMeta.Value> values = entityMeta.getPropertyMetas().stream()
				.filter(a -> a.isAutoIncrement() == false)
				.map(a -> a.getValue(entity))
				.collect(Collectors.toList());

		if (excludesNull) {
			values = values.stream()
					.filter(a -> a.isNull() == false || a.getPropertyMeta().isVersion())
					.collect(Collectors.toList());
		}

		for (Iterator<PropertyMeta.Value> iterator = values.iterator(); iterator.hasNext();) {
			PropertyMeta.Value value = iterator.next();
			query1.append(value.getPropertyMeta().getColumnName());
			query2.append("?");
			if (iterator.hasNext()) {
				query1.append(", ");
				query2.append(", ");
			}
		}
		String sql = query1.append(query2).append(")").toString();
		int insertedCount;
		List<SqlParameterValue> sqlParameterValues = new ArrayList<>();
		for (PropertyMeta.Value value : values) {
			if (value.getPropertyMeta().isVersion()) {
				sqlParameterValues.add(value.getPropertyMeta().toInitialVersionSqlParameterValue());
			} else {
				sqlParameterValues.add(value.toSqlParameterValue());
			}
		}
		Object[] args = sqlParameterValues.toArray();
		insertedCount = jdbcOperations.update(sql, args);
		if (entityMeta.hasAutoIncrement()) {
			for (PropertyMeta propertyMeta : entityMeta.getPropertyMetas()) {
				if (propertyMeta.isAutoIncrement() == false) {
					continue;
				}
				Object value = jdbcOperations.queryForObject(
						"select currval(pg_get_serial_sequence(?, ?))",
						propertyMeta.getJavaType(),
						entityMeta.getTableName(),
						propertyMeta.getColumnName());
				propertyMeta.bindAutoIncrementValue(entity, value);
			}
		}
		if (entityMeta.hasVersion()) {
			for (PropertyMeta propertyMeta : entityMeta.getPropertyMetas()) {
				if (propertyMeta.isVersion() == false) {
					continue;
				}
				propertyMeta.bindInitialVersion(entity);
			}
		}
		return insertedCount;
	}

	@Override
	public int update(Object entity) {
		return update(entity, false, false);
	}

	@Override
	public int updateExcludesNull(Object entity) {
		return update(entity, true, false);
	}

	@Override
	public int forceVersioningUpdate(Object entity) {
		return update(entity, false, true);
	}

	@Override
	public int forceVersioningUpdateExcludesNull(Object entity) {
		return update(entity, true, true);
	}

	private int update(Object entity, boolean excludesNull, boolean forceVersioning) {
		EntityMeta entityMeta = entityMetaFactory.create(entity);

		StringBuilder query = new StringBuilder();
		query.append("update ").append(entityMeta.getTableName()).append(" set ");

		List<PropertyMeta.Value> values = entityMeta.getPropertyMetas().stream()
				.filter(a -> a.isPrimaryKey() == false)
				.map(a -> a.getValue(entity))
				.collect(Collectors.toList());

		if (excludesNull) {
			values = values.stream()
					.filter(a -> a.isNull() == false || a.getPropertyMeta().isVersion())
					.collect(Collectors.toList());
		}

		for (Iterator<PropertyMeta.Value> iterator = values.iterator(); iterator.hasNext();) {
			PropertyMeta.Value value = iterator.next();
			query.append(value.getPropertyMeta().getColumnName());
			if (value.getPropertyMeta().isVersion()) {
				query.append(" = ").append(value.getPropertyMeta().getColumnName()).append(" + 1");
			} else {
				query.append(" = ?");
			}
			if (iterator.hasNext()) {
				query.append(", ");
			}
		}

		query.append(" where ");

		List<PropertyMeta.Value> primaryKeyValues = entityMeta.getPropertyMetas().stream()
				.filter(a -> a.isPrimaryKey()).map(a -> a.getValue(entity)).collect(Collectors.toList());
		if (primaryKeyValues.isEmpty()) {
			throw new AutoCudException();
		}
		for (Iterator<PropertyMeta.Value> iterator = primaryKeyValues.iterator(); iterator.hasNext();) {
			PropertyMeta.Value value = iterator.next();
			query.append(value.getPropertyMeta().getColumnName()).append(" = ?");
			if (iterator.hasNext()) {
				query.append(" and ");
			}
		}

		List<PropertyMeta.Value> versionValues = entityMeta.getPropertyMetas().stream()
				.filter(a -> a.isVersion()).map(a -> a.getValue(entity)).collect(Collectors.toList());
		if (forceVersioning == false) {
			Iterator<PropertyMeta.Value> iterator = versionValues.iterator();
			if (iterator.hasNext()) {
				query.append(" and ");
				for (; iterator.hasNext();) {
					PropertyMeta.Value value = iterator.next();
					query.append(value.getPropertyMeta().getColumnName()).append(" = ?");
					if (iterator.hasNext()) {
						query.append(" and ");
					}
				}
			}
		}

		String sql = query.toString();

		int updatedCount;
		List<SqlParameterValue> sqlParameterValues = new ArrayList<>();
		for (PropertyMeta.Value value : values) {
			if (value.getPropertyMeta().isVersion() == false) {
				sqlParameterValues.add(value.toSqlParameterValue());
			}
		}
		for (PropertyMeta.Value value : primaryKeyValues) {
			sqlParameterValues.add(value.toSqlParameterValue());
		}
		if (forceVersioning == false) {
			for (PropertyMeta.Value value : versionValues) {
				sqlParameterValues.add(value.toSqlParameterValue());
			}
		}
		Object[] args = sqlParameterValues.toArray();
		updatedCount = jdbcOperations.update(sql, args);
		if (entityMeta.hasVersion() && forceVersioning == false) {
			for (PropertyMeta propertyMeta : entityMeta.getPropertyMetas()) {
				if (propertyMeta.isVersion() == false) {
					continue;
				}
				propertyMeta.incrementVersion(entity);
			}
		}
		return updatedCount;
	}

	@Override
	public int delete(Object entity) {
		EntityMeta entityMeta = entityMetaFactory.create(entity);

		StringBuilder query = new StringBuilder();
		query.append("delete from ").append(entityMeta.getTableName()).append(" where ");

		List<PropertyMeta.Value> values = entityMeta.getPropertyMetas().stream()
				.filter(a -> a.isPrimaryKey())
				.map(a -> a.getValue(entity))
				.collect(Collectors.toList());

		if (values.isEmpty()) {
			throw new AutoCudException();
		}

		for (Iterator<PropertyMeta.Value> iterator = values.iterator(); iterator.hasNext();) {
			PropertyMeta.Value value = iterator.next();
			query.append(value.getPropertyMeta().getColumnName()).append(" = ?");
			if (iterator.hasNext()) {
				query.append(" and ");
			}
		}
		String sql = query.toString();
		Object[] args = values.stream().map(a -> a.toSqlParameterValue()).toArray();

		int deletedCount = jdbcOperations.update(sql, args);
		return deletedCount;
	}
}
