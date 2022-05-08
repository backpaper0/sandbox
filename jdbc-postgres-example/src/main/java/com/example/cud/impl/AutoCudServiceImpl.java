package com.example.cud.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.example.cud.AutoCudException;
import com.example.cud.AutoCudService;
import com.example.cud.EntityMeta;
import com.example.cud.EntityMetaFactory;
import com.example.cud.PropertyMeta;

public class AutoCudServiceImpl implements AutoCudService {

	private final DataSource dataSource;
	private final EntityMetaFactory entityMetaFactory;

	public AutoCudServiceImpl(DataSource dataSource, EntityMetaFactory entityMetaFactory) {
		this.dataSource = dataSource;
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
		try {
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
			try (Connection con = dataSource.getConnection()) {
				try (PreparedStatement pst = con.prepareStatement(sql)) {
					int index = 1;
					for (PropertyMeta.Value value : values) {
						if (value.getPropertyMeta().isVersion()) {
							value.getPropertyMeta().bindInitialVersion(pst, index);
						} else {
							value.bind(pst, index);
						}
						index++;
					}
					insertedCount = pst.executeUpdate();
				}
				if (entityMeta.hasAutoIncrement()) {
					try (PreparedStatement pst = con.prepareStatement("select currval(pg_get_serial_sequence(?, ?))")) {
						for (PropertyMeta propertyMeta : entityMeta.getPropertyMetas()) {
							if (propertyMeta.isAutoIncrement() == false) {
								continue;
							}
							pst.setString(1, entityMeta.getTableName());
							pst.setString(2, propertyMeta.getColumnName());
							try (ResultSet rs = pst.executeQuery()) {
								if (rs.next() == false) {
									throw new RuntimeException();
								}
								propertyMeta.bindAutoIncrementValue(entity, rs);
							}
						}
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
			}
			return insertedCount;
		} catch (SQLException e) {
			throw new AutoCudException(e);
		}
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
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement pst = con.prepareStatement(sql)) {
				int index = 1;
				for (PropertyMeta.Value value : values) {
					if (value.getPropertyMeta().isVersion() == false) {
						value.bind(pst, index++);
					}
				}
				for (PropertyMeta.Value value : primaryKeyValues) {
					value.bind(pst, index++);
				}
				if (forceVersioning == false) {
					for (PropertyMeta.Value value : versionValues) {
						value.bind(pst, index++);
					}
				}
				updatedCount = pst.executeUpdate();
			}
			if (entityMeta.hasVersion() && forceVersioning == false) {
				for (PropertyMeta propertyMeta : entityMeta.getPropertyMetas()) {
					if (propertyMeta.isVersion() == false) {
						continue;
					}
					propertyMeta.incrementVersion(entity);
				}
			}
		} catch (SQLException e) {
			throw new AutoCudException(e);
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

		for (Iterator<PropertyMeta.Value> iterator = values.iterator(); iterator.hasNext();) {
			PropertyMeta.Value value = iterator.next();
			query.append(value.getPropertyMeta().getColumnName()).append(" = ?");
			if (iterator.hasNext()) {
				query.append(" and ");
			}
		}
		String sql = query.toString();

		int deletedCount;
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement pst = con.prepareStatement(sql)) {
				int index = 1;
				for (PropertyMeta.Value value : values) {
					value.bind(pst, index++);
				}
				deletedCount = pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new AutoCudException(e);
		}
		return deletedCount;
	}
}
