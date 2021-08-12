package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariDataSource;

public class HibernateTransactionTimeoutTest {

	private EntityManagerFactory emf;
	private String tagId;
	private Connection con;
	private HikariDataSource dataSource;
	private ScheduledExecutorService executor;
	private EntityManager em;

	@Test
	void find() {
		assertThrows(PersistenceException.class,
				() -> em.find(Tag.class, tagId, LockModeType.PESSIMISTIC_READ));
	}

	@Test
	void jpql() {
		assertThrows(PersistenceException.class, () -> em
				.createQuery("select t from Tag t", Tag.class)
				.setLockMode(LockModeType.PESSIMISTIC_READ)
				.getResultList());
	}

	@Test
	void nativeQuery() {
		assertThrows(PersistenceException.class, () -> em
				.createNativeQuery("select * from Tag for share")
				.getResultList());
	}

	@Test
	void namedNativeQuery() {
		assertThrows(PersistenceException.class, () -> em
				.createNamedQuery("Tag.findAll.native")
				.getResultList());
	}

	@Test
	void jpqlUpdate() {
		assertThrows(PersistenceException.class, () -> em
				.createQuery("update Tag t set t.name = :name where t.id = :id")
				.setParameter("name", "Updated")
				.setParameter("id", tagId)
				.executeUpdate());
	}

	@Test
	void jpqlDelete() {
		assertThrows(PersistenceException.class, () -> em
				.createQuery("delete from Tag t where t.id = :id")
				.setParameter("id", tagId)
				.executeUpdate());
	}

	@Test
	void criteria() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tag> q = cb.createQuery(Tag.class);
		Root<Tag> tag = q.from(Tag.class);
		q.select(tag);

		assertThrows(PersistenceException.class,
				() -> em.createQuery(q).setLockMode(LockModeType.PESSIMISTIC_READ).getResultList());
	}

	@Test
	void criteriaUpdate() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<Tag> update = cb.createCriteriaUpdate(Tag.class);
		Root<Tag> tag = update.from(Tag.class);
		update.set(tag.get("name"), "Updated")
				.where(cb.equal(tag.get("id"), tagId));

		assertThrows(PersistenceException.class, () -> em.createQuery(update).executeUpdate());
	}

	@Test
	void criteriaDelete() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<Tag> delete = cb.createCriteriaDelete(Tag.class);
		Root<Tag> tag = delete.from(Tag.class);
		delete.where(cb.equal(tag.get("id"), tagId));

		assertThrows(PersistenceException.class, () -> em.createQuery(delete).executeUpdate());
	}

	@Test
	void update() {
		Tag tag = em.find(Tag.class, tagId);
		tag.setName("Updated");
		assertThrows(PersistenceException.class, () -> em.getTransaction().commit());
	}

	@Test
	void remove() {
		Tag tag = em.find(Tag.class, tagId);
		em.remove(tag);
		assertThrows(PersistenceException.class, () -> em.getTransaction().commit());
	}

	@Test
	void merge() {
		Tag tag = new Tag();
		tag.setId(tagId);
		tag.setName("Updated");
		em.merge(tag);
		assertThrows(PersistenceException.class, () -> em.getTransaction().commit());
	}

	@BeforeEach
	void init() throws SQLException {
		dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/demodb");
		dataSource.setUsername("demo");
		dataSource.setPassword("secret");
		Map<String, Object> props = new HashMap<>();
		props.put(AvailableSettings.DATASOURCE, dataSource);
		//props.put(QueryHints.TIMEOUT_JPA, 3000);
		//props.put(AvailableSettings.JPA_LOCK_TIMEOUT, 2000);

		emf = Persistence.createEntityManagerFactory("demo", props);

		// テストデータ準備
		em = emf.createEntityManager();
		em.getTransaction().begin();
		Tag tag = Tag.create("Hibernate");
		tagId = tag.getId();
		em.persist(tag);
		em.getTransaction().commit();

		// ロックを取得するためJDBCのAPIでアップデートをする
		con = dataSource.getConnection();
		con.setAutoCommit(false);
		try (PreparedStatement pst = con.prepareStatement("update Tag set name = ? where id = ?")) {
			pst.setString(1, "Updated");
			pst.setString(2, tagId);
			pst.executeUpdate();
		}

		em.clear();

		// トランザクションタイムアウトを設定してトランザクション(2秒)を開始する
		em.unwrap(Session.class).getTransaction().setTimeout(2);
		em.getTransaction().begin();

		// テストが5秒超えたらロールバックする
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.schedule(() -> {
			if (con != null && con.isClosed() == false) {
				con.rollback();
				con.setAutoCommit(false);
				con.close();
			}
			return null;
		}, 5, TimeUnit.SECONDS);
	}

	@AfterEach
	void destroy() throws SQLException {
		if (em != null) {
			EntityTransaction tx = em.getTransaction();
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		}
		if (con != null && con.isClosed() == false) {
			con.rollback();
			con.close();
		}
		if (emf != null) {
			emf.close();
		}
		if (dataSource != null) {
			dataSource.close();
		}
		if (executor != null) {
			executor.shutdown();
		}
	}
}
