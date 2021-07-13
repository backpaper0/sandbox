package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TransactionTimeoutTest {

	private static EntityManagerFactory emf;
	private static String tagId1;
	private static String tagId2;

	@BeforeAll
	static void init() throws Exception {
		emf = Persistence.createEntityManagerFactory("demo");

		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			try {
				Tag tag1 = Tag.create("Java");
				tagId1 = tag1.getId();
				em.persist(tag1);

				Tag tag2 = Tag.create("JPA");
				tagId2 = tag2.getId();
				em.persist(tag2);

				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				throw e;
			}
		} finally {
			em.close();
		}
	}

	@Test
	void transactionTimeout() throws Exception {
		EntityManager em = emf.createEntityManager();
		try (Session session = em.unwrap(Session.class)) {
			Transaction tx = session.getTransaction();
			tx.setTimeout(2);
			tx.begin();
			try {
				Tag tag1 = em.find(Tag.class, tagId1);
				assertEquals("Java", tag1.getName());

				TimeUnit.SECONDS.sleep(3);

				PersistenceException e = assertThrows(PersistenceException.class,
						() -> em.find(Tag.class, tagId2));
				assertEquals(TransactionException.class, e.getCause().getClass());
				assertEquals("transaction timeout expired", e.getCause().getMessage());
			} finally {
				tx.rollback();
			}
		}
	}

	@Test
	void noTransactionTimeout() throws Exception {
		EntityManager em = emf.createEntityManager();
		try (Session session = em.unwrap(Session.class)) {
			Transaction tx = session.getTransaction();
			//			tx.setTimeout(2);
			tx.begin();
			try {
				Tag tag1 = em.find(Tag.class, tagId1);
				assertEquals("Java", tag1.getName());

				TimeUnit.SECONDS.sleep(3);

				Tag tag2 = em.find(Tag.class, tagId2);
				assertEquals("JPA", tag2.getName());
			} finally {
				tx.rollback();
			}
		}
	}
}
