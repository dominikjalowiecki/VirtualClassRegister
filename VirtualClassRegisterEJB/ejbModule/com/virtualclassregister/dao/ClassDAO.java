package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.User;

@Stateless
public class ClassDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Class _class) {
		em.persist(_class);
	}

	public Class merge(Class _class) {
		return em.merge(_class);
	}

	public void remove(Class _class) {
		em.remove(em.merge(_class));
	}

	public Class find(Object id) {
		return em.find(Class.class, id);
	}

	public List<Class> getFullList() {
		List<Class> list = null;

		Query query = em.createQuery("SELECT c FROM Class c ORDER BY name ASC");

		list = query.getResultList();

		return list;
	}
	
	public List<Class> getList(Map<String, String> searchParams) {
		List<Class> list = null;

		String select = "SELECT c ";
		String from = "FROM Class c ";
		String where = "";
		String orderby = "ORDER BY c.name ASC";

		for (Map.Entry<String, String> set : searchParams.entrySet()) {
			if (where.isEmpty()) {
				where = "WHERE ";
			} else {
				where += "AND ";
			}
			where += "c." + set.getKey() + " = :" + set.getKey() + " ";
		}
		
		Query query = em.createQuery(select + from + where + orderby);
		
		for (Map.Entry<String, String> set : searchParams.entrySet()) {
			query.setParameter(set.getKey(), set.getValue());
		}

		list = query.getResultList();

		return list;
	}

}
