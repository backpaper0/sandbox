package com.example;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class EntityManagerProvider {

	@PersistenceContext(unitName = "example")
	private EntityManager em;

	@Produces
	@RequestScoped
	public EntityManager entityManager() {
		return em;
	}
}
