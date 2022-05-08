package com.example.cud.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.example.cud.EntityMeta;
import com.example.cud.EntityMetaFactory;

public class InMemoryCacheEntityMetaFactory implements EntityMetaFactory {

	private final EntityMetaFactory entityMetaFactory;
	private final ConcurrentMap<Class<?>, EntityMeta> cache = new ConcurrentHashMap<>();

	public InMemoryCacheEntityMetaFactory(EntityMetaFactory entityMetaFactory) {
		this.entityMetaFactory = entityMetaFactory;
	}

	@Override
	public EntityMeta create(Class<?> entityClass) {
		return cache.computeIfAbsent(entityClass, entityMetaFactory::create);
	}
}
