package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.virtualclassregister.entities.Class;
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

		Query query = em.createQuery("SELECT s FROM Subject s ORDER BY name ASC");

		list = query.getResultList();

		return list;
	}
	
	public List<Subject> getList(Map<String, String> searchParams) {
		List<Subject> list = null;

		String select = "SELECT s ";
		String from = "FROM Subject s ";
		String where = "";
		String orderby = "ORDER BY s.name ASC";

		for (Map.Entry<String, String> set : searchParams.entrySet()) {
			if (where.isEmpty()) {
				where = "WHERE ";
			} else {
				where += "AND ";
			}
			where += "s." + set.getKey() + " = :" + set.getKey() + " ";
		}
		
		Query query = em.createQuery(select + from + where + orderby);
		
		for (Map.Entry<String, String> set : searchParams.entrySet()) {
			query.setParameter(set.getKey(), set.getValue());
		}

		list = query.getResultList();

		return list;
	}

}
