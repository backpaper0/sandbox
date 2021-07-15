package com.example;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Branch {

	@Id
	private String id;
	private String name;
	@Version
	private Integer version;

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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public static Branch create(String name) {
		Branch branch = new Branch();
		branch.setId(UUID.randomUUID().toString());
		branch.setName(name);
		return branch;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, version);
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
		Branch other = (Branch) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(version, other.version);
	}

	@Override
	public String toString() {
		return name;
	}
}
