package com.example;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Repository extends Base2 {

    @Id
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Repository create(String name) {
        Repository repo = new Repository();
        repo.setId(UUID.randomUUID().toString());
        repo.setName(name);
        return repo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, getVersion());
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
        Repository other = (Repository) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(getVersion(), other.getVersion());
    }

    @Override
    public String toString() {
        return name;
    }
}
