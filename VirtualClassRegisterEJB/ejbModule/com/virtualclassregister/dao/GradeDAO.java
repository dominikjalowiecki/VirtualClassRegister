package com.virtualclassregister.dao;

import java.util.List;

import com.virtualclassregister.entities.Grade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class GradeDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Grade grade) {
		em.persist(grade);
	}

	public Grade merge(Grade grade) {
		return em.merge(grade);
	}

	public void remove(Grade grade) {
		em.remove(em.merge(grade));
	}

	public Grade find(Object id) {
		return em.find(Grade.class, id);
	}

	public List<Grade> getFullList() {
		List<Grade> list = null;

		Query query = em.createQuery("SELECT g FROM Grade g");

		list = query.getResultList();

		return list;
	}

}
