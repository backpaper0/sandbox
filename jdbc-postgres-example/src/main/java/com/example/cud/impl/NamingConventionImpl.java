package com.example.cud.impl;

import java.util.PrimitiveIterator;

import com.example.cud.NamingConvention;

public class NamingConventionImpl implements NamingConvention {

	@Override
	public String convertToTableName(String entityName) {
		StringBuilder sb = new StringBuilder();
		PrimitiveIterator.OfInt iterator = entityName.codePoints().iterator();
		if (iterator.hasNext()) {
			int codePoint = iterator.nextInt();
			sb.appendCodePoint(Character.toLowerCase(codePoint));
			while (iterator.hasNext()) {
				codePoint = iterator.nextInt();
				if (Character.isUpperCase(codePoint)) {
					sb.append('_').appendCodePoint(Character.toLowerCase(codePoint));
				} else {
					sb.appendCodePoint(codePoint);
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String convertToColumnName(String propertyName) {
		return convertToTableName(propertyName);
	}
}
