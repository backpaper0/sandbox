package com.example.cud;

public interface EntityListener {

	default void preInsert(Object entity) {
	}

	default void preUpdate(Object entity) {
	}
}
