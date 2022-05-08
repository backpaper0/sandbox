package com.example.cud;

import java.util.List;

public interface EntityMeta {

	String getTableName();

	List<PropertyMeta> getPropertyMetas();

	PropertyMeta getVersion();

	boolean hasAutoIncrement();

	boolean hasVersion();
}
