package com.example;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListenerDefinedInOrmXmlTest {

    @Test
    void entity() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Committer committer = Committer.create("foo");
            em.persist(committer);
            em.flush();

            assertEquals(0, committer.getVersion());

            em.clear();

            committer = em.find(Committer.class, committer.getId());

            assertEquals(0, committer.getVersion());

            committer.setName("bar");

            em.flush();

            assertEquals(1, committer.getVersion());

            tx.commit();
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        }
    }

    @Test
    void mappedSuperclass() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Author author = Author.create("foo");
            em.persist(author);
            em.flush();

            assertEquals(0, author.getVersion());

            em.clear();

            author = em.find(Author.class, author.getId());

            assertEquals(0, author.getVersion());

            author.setName("bar");

            em.flush();

            assertEquals(1, author.getVersion());

            tx.commit();
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        }
    }

    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void init() {
        em = emf.createEntityManager();
    }

    @AfterEach
    void destroy() {
        if (em != null) {
            em.close();
        }
    }

    @BeforeAll
    static void initEmf() {
        emf = Persistence.createEntityManagerFactory("demo");
    }

    @AfterAll
    static void destroyEmf() {
        if (emf != null) {
            emf.close();
        }
    }
}
