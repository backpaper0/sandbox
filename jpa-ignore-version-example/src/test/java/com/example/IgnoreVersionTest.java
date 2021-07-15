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

    // metadata-complete="false"の場合
    @Test
    void ignoreVersionMappedSuperclass() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        String id;
        try {
            Commit commit = Commit.create("0312b1720af275ab895d1f6b23728a74c0da798d");
            id = commit.getId();
            em.persist(commit);
            tx.commit();
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        }
        Commit commit = em.find(Commit.class, id);
        assertNull(commit.getVersion());
    }

    // metadata-completeを指定していない場合
    @Test
    void notIgnoreVersionMappedSuperclass() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        String id;
        try {
            Repository repo = Repository.create("backpaper0/sandbox");
            id = repo.getId();
            em.persist(repo);
            tx.commit();
        } catch (Throwable t) {
            tx.rollback();
            throw t;
        }
        Repository repo = em.find(Repository.class, id);
        assertNotNull(repo.getVersion());
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
