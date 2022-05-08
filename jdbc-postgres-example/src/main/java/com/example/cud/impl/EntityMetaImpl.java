package com.example.cud.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.util.Assert;

import com.example.cud.EntityMeta;
import com.example.cud.PropertyMeta;

public class EntityMetaImpl implements EntityMeta {

	private final String tableName;
	private final List<PropertyMeta> propertyMetas;
	private final boolean hasAutoIncrement;
	private final PropertyMeta versionPropertyMeta;

	public EntityMetaImpl(String tableName, List<PropertyMeta> propertyMetas) {
		this.tableName = tableName;
		this.propertyMetas = propertyMetas;
		this.hasAutoIncrement = propertyMetas.stream().anyMatch(PropertyMeta::isAutoIncrement);
		this.versionPropertyMeta = findVersion(propertyMetas);
	}

	private static PropertyMeta findVersion(List<PropertyMeta> propertyMetas) {
		Iterator<PropertyMeta> versionPropertyMetas = propertyMetas.stream().filter(PropertyMeta::isVersion)
				.iterator();
		if (versionPropertyMetas.hasNext()) {
			PropertyMeta propertyMeta = versionPropertyMetas.next();
			Assert.state(versionPropertyMetas.hasNext() == false, "version column must be one column");
			return propertyMeta;
		}
		return null;
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
	public PropertyMeta getVersion() {
		return versionPropertyMeta;
	}

	@Override
	public boolean hasAutoIncrement() {
		return hasAutoIncrement;
	}

	@Override
	public boolean hasVersion() {
		return versionPropertyMeta != null;
	}
}
