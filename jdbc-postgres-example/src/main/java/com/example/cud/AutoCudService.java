package com.example.cud;

public interface AutoCudService {

	int insert(Object entity);

	int insertExcludesNull(Object entity);

	int update(Object entity);

	int updateExcludesNull(Object entity);

	int forceVersioningUpdate(Object entity);

	int forceVersioningUpdateExcludesNull(Object entity);

	int delete(Object entity);
}
