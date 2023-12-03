package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import com.virtualclassregister.entities.Class;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

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
	
	public List<Class> getList(Map<String, Object> searchParams) {
		List<Class> list = null;
		
		Query query = prepareQuery(searchParams);

		list = query.getResultList();

		return list;
	}
	
	public int getCount(Map<String, Object> searchParams) {
		String select = "SELECT COUNT(c) FROM Class c ";
		String where = "";
		
		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "c.";
				
				String words[] = key.split("\\s+");
				
				if(words.length == 2 && words[0].equals("like")) {
					where += words[1] + " LIKE :" + words[1] + " ";
				} else {
					where += key + " = :" + key + " ";
				}
			}
		}
		
		Query query = em.createQuery(select + where);
		
		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				String key = set.getKey();				
				String words[] = key.split("\\s+");
				
				if(words.length == 2 && words[0].equals("like")) {
					query.setParameter(words[1], "%" + set.getValue() + "%");
				} else {
					query.setParameter(key, set.getValue());
				}
			}
		}
		
		int count = ((Long) query.getSingleResult()).intValue();
		return count;
	}
	
	public List<Class> getList(Map<String, Object> searchParams, int offset, int pageSize) {		
		List<Class> list = null;

		Query query = prepareQuery(searchParams);

		list = query
				.setMaxResults(pageSize)
				.setFirstResult(offset)
				.getResultList();

		return list;
	}
	
	private Query prepareQuery(Map<String, Object> searchParams) {
		String select = "SELECT c ";
		String from = "FROM Class c ";
		String where = "";
		String orderby = "ORDER BY c.name ASC";

		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "c.";
				
				String words[] = key.split("\\s+");
				
				if(words.length == 2 && words[0].equals("like")) {
					where += words[1] + " LIKE :" + words[1] + " ";
				} else {
					where += key + " = :" + key + " ";
				}
			}
		}
		
		Query query = em.createQuery(select + from + where + orderby);
		
		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				String key = set.getKey();				
				String words[] = key.split("\\s+");
				
				if(words.length == 2 && words[0].equals("like")) {
					query.setParameter(words[1], "%" + set.getValue() + "%");
				} else {
					query.setParameter(key, set.getValue());
				}
			}
		}
		
		return query;
	}

}
