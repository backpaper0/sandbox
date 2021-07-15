package com.example;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Commit extends Base1 {

    @Id
    private String id;
    private String hash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public static Commit create(String hash) {
        Commit commit = new Commit();
        commit.setId(UUID.randomUUID().toString());
        commit.setHash(hash);
        return commit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hash, getVersion());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        Commit other = (Commit) obj;
        return Objects.equals(id, other.id) && Objects.equals(hash, other.hash)
                && Objects.equals(getVersion(), other.getVersion());
    }

    @Override
    public String toString() {
        return hash;
    }
}
