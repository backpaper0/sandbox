package com.example.cud.impl;

import java.util.List;

import com.example.cud.EntityMeta;
import com.example.cud.PropertyMeta;

public class EntityMetaImpl implements EntityMeta {

	private final String tableName;
	private final List<PropertyMeta> propertyMetas;
	private final boolean hasAutoIncrement;
	private final boolean hasVersion;

	public EntityMetaImpl(String tableName, List<PropertyMeta> propertyMetas) {
		this.tableName = tableName;
		this.propertyMetas = propertyMetas;
		this.hasAutoIncrement = propertyMetas.stream().anyMatch(PropertyMeta::isAutoIncrement);
		this.hasVersion = propertyMetas.stream().anyMatch(PropertyMeta::isVersion);
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public List<PropertyMeta> getPropertyMetas() {
		return propertyMetas;
	}

	@Override
	public boolean hasAutoIncrement() {
		return hasAutoIncrement;
	}

	@Override
	public boolean hasVersion() {
		return hasVersion;
	}
}
