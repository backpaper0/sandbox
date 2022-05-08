package com.example.cud.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcOperations;

import com.example.cud.AutoCudException;
import com.example.cud.AutoCudService;
import com.example.cud.EntityMeta;
import com.example.cud.EntityMetaFactory;
import com.example.cud.OptimisticLockException;
import com.example.cud.PropertyMeta;

public class AutoCudServiceImpl implements AutoCudService {

	private static final Predicate<PropertyMeta.Value> isNotAutoIncrement = a -> a.getPropertyMeta()
			.isAutoIncrement() == false;
	private static final Predicate<PropertyMeta.Value> isNotNull = a -> a.isNull() == false;
	private static final Predicate<PropertyMeta.Value> isVersion = a -> a.getPropertyMeta().isVersion();
	private static final Predicate<PropertyMeta.Value> isNotVersion = isVersion.negate();
	private static final Predicate<PropertyMeta.Value> isNotPrimaryKey = a -> a.getPropertyMeta()
			.isPrimaryKey() == false;

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
		EntityMeta entityMeta = entityMetaFactory.create(entity.getClass());

		Predicate<PropertyMeta.Value> predicate = isNotAutoIncrement;
		if (excludesNull) {
			predicate = predicate.and(isNotNull.or(isVersion));
		}

		List<PropertyMeta.Value> values = entityMeta.getPropertyMetas().stream()
				.map(a -> a.getValue(entity))
				.map(PropertyMeta.Value::convertInitialVersionValueIfVersion)
				.filter(predicate)
				.collect(Collectors.toList());

		StringBuilder query1 = new StringBuilder();
		StringBuilder query2 = new StringBuilder();
		query1.append("insert into ").append(entityMeta.getTableName()).append("(");
		query2.append(") values (");
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

		Object[] args = values
				.stream().map(PropertyMeta.Value::toSqlParameterValue)
				.toArray();

		int insertedCount = jdbcOperations.update(sql, args);

		if (entityMeta.hasAutoIncrement()) {
			List<PropertyMeta> propertyMetas = entityMeta.getPropertyMetas().stream()
					.filter(PropertyMeta::isAutoIncrement)
					.collect(Collectors.toList());
			for (PropertyMeta propertyMeta : propertyMetas) {
				Object value = jdbcOperations.queryForObject(
						"select currval(pg_get_serial_sequence(?, ?))",
						propertyMeta.getJavaType(),
						entityMeta.getTableName(),
						propertyMeta.getColumnName());
				propertyMeta.bindValue(entity, value);
			}
		}
		if (entityMeta.hasVersion()) {
			PropertyMeta propertyMeta = entityMeta.getVersion();
			propertyMeta.bindInitialVersion(entity);
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
		EntityMeta entityMeta = entityMetaFactory.create(entity.getClass());

		StringBuilder query = new StringBuilder();
		query.append("update ").append(entityMeta.getTableName()).append(" set ");

		Predicate<PropertyMeta.Value> predicate = isNotPrimaryKey.and(isNotVersion);
		if (excludesNull) {
			predicate = predicate.and(isNotNull);
		}

		List<PropertyMeta.Value> values = entityMeta.getPropertyMetas().stream()
				.map(a -> a.getValue(entity))
				.filter(predicate)
				.collect(Collectors.toList());

		for (Iterator<PropertyMeta.Value> iterator = values.iterator(); iterator.hasNext();) {
			PropertyMeta.Value value = iterator.next();
			query.append(value.getPropertyMeta().getColumnName()).append(" = ?");
			if (iterator.hasNext()) {
				query.append(", ");
			}
		}

		if (entityMeta.hasVersion()) {
			String columnName = entityMeta.getVersion().getColumnName();
			query.append(", ").append(columnName).append(" = ").append(columnName).append(" + 1");
		}

		query.append(" where ");

		List<PropertyMeta.Value> primaryKeyValues = entityMeta.getPropertyMetas().stream()
				.filter(PropertyMeta::isPrimaryKey).map(a -> a.getValue(entity)).collect(Collectors.toList());
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

		boolean useVersion = forceVersioning == false && entityMeta.hasVersion();
		if (useVersion) {
			PropertyMeta.Value value = entityMeta.getVersion().getValue(entity);
			query.append(" and ").append(value.getPropertyMeta().getColumnName()).append(" = ?");
		}

		String sql = query.toString();

		List<PropertyMeta.Value> arguments = new ArrayList<>();
		arguments.addAll(values);
		arguments.addAll(primaryKeyValues);
		if (useVersion) {
			arguments.add(entityMeta.getVersion().getValue(entity));
		}
		Object[] args = arguments.stream().map(PropertyMeta.Value::toSqlParameterValue).toArray();

		int updatedCount = jdbcOperations.update(sql, args);
		if (updatedCount == 0) {
			throw new OptimisticLockException();
		}
		if (useVersion) {
			entityMeta.getVersion().bindIncrementVersion(entity);
		}
		return updatedCount;
	}

	@Override
	public int delete(Object entity) {
		EntityMeta entityMeta = entityMetaFactory.create(entity.getClass());

		StringBuilder query = new StringBuilder();
		query.append("delete from ").append(entityMeta.getTableName()).append(" where ");

		List<PropertyMeta.Value> values = entityMeta.getPropertyMetas().stream()
				.filter(PropertyMeta::isPrimaryKey)
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
		Object[] args = values.stream().map(PropertyMeta.Value::toSqlParameterValue).toArray();

		return jdbcOperations.update(sql, args);
	}
}
