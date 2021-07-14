package com.example;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IgnoreVersionTest {

	// アノテーションで定義されたバージョンプロパティを
	// orm.xmlでオーバーライドして無効化する例。
	// entity要素にmetadata-complete属性を設定することで
	// オーバーライドする・しないが決まる。

	// metadata-complete="false"の場合
	@Test
	void ignoreVersion() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		String id;
		try {
			Tag tag = Tag.create("1.0");
			id = tag.getId();
			em.persist(tag);
			tx.commit();
		} catch (Throwable t) {
			tx.rollback();
			throw t;
		}
		Tag tag = em.find(Tag.class, id);
		assertNull(tag.getVersion());
	}

	// metadata-completeを指定していない場合
	@Test
	void notIgnoreVersion() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		String id;
		try {
			Branch branch = Branch.create("1.0");
			id = branch.getId();
			em.persist(branch);
			tx.commit();
		} catch (Throwable t) {
			tx.rollback();
			throw t;
		}
		Branch branch = em.find(Branch.class, id);
		assertNotNull(branch.getVersion());
	}

	private EntityManagerFactory emf;
	private EntityManager em;

	@BeforeEach
	void init() {
		emf = Persistence.createEntityManagerFactory("demo");
		em = emf.createEntityManager();
	}

	@AfterEach
	void destroy() {
		if (em != null) {
			em.close();
		}
		if (emf != null) {
			emf.close();
		}
	}
}
