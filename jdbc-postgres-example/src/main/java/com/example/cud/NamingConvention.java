package com.example.cud;

public interface NamingConvention {

	String convertToTableName(String entityName);

	String convertToColumnName(String propertyName);
}
