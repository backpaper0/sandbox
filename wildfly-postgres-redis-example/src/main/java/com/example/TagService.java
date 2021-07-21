package com.example;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class TagService {

	@Inject
	private EntityManager em;

	public Tags findAll() {
		List<Tag> tags = em.createNamedQuery("findAll", Tag.class).getResultList();
		return new Tags(tags);
	}

	public Tag create(String name) {
		Tag tag = new Tag();
		tag.setName(name);
		em.persist(tag);
		return tag;
	}
}
