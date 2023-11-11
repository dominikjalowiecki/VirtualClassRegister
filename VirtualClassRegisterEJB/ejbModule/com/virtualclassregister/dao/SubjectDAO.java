package com.virtualclassregister.dao;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.virtualclassregister.entities.Subject;

@Stateless
public class SubjectDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Subject subject) {
		em.persist(subject);
	}

	public Subject merge(Subject subject) {
		return em.merge(subject);
	}

	public void remove(Subject subject) {
		em.remove(em.merge(subject));
	}

	public Subject find(Object id) {
		return em.find(Subject.class, id);
	}

	public List<Subject> getFullList() {
		List<Subject> list = null;

		Query query = em.createQuery("SELECT s FROM Subject s");

		list = query.getResultList();

		return list;
	}

}
