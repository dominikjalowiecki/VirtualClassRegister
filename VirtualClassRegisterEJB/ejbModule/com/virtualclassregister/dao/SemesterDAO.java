package com.virtualclassregister.dao;

import java.util.List;

import com.virtualclassregister.entities.Semester;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class SemesterDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Semester semester) {
		em.persist(semester);
	}

	public Semester merge(Semester semester) {
		return em.merge(semester);
	}

	public void remove(Semester semester) {
		em.remove(em.merge(semester));
	}

	public Semester find(Object id) {
		return em.find(Semester.class, id);
	}

	public List<Semester> getFullList() {
		List<Semester> list = null;

		Query query = em.createQuery("SELECT s FROM Semester s ORDER BY start DESC");

		list = query.getResultList();

		return list;
	}

}
