package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import com.virtualclassregister.entities.Subject;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

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
	
	public List<Subject> getList(Map<String, Object> searchParams) {
		List<Subject> list = null;
		
		Query query = prepareQuery(searchParams);

		list = query.getResultList();

		return list;
	}
	
	public int getCount(Map<String, Object> searchParams) {
		String select = "SELECT COUNT(s) FROM Subject s ";
		String where = "";
		
		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "s.";
				
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
	
	public List<Subject> getList(Map<String, Object> searchParams, int offset, int pageSize) {		
		List<Subject> list = null;

		Query query = prepareQuery(searchParams);

		list = query
				.setMaxResults(pageSize)
				.setFirstResult(offset)
				.getResultList();

		return list;
	}
	
	private Query prepareQuery(Map<String, Object> searchParams) {
		String select = "SELECT s ";
		String from = "FROM Subject s ";
		String where = "";
		String orderby = "ORDER BY s.name ASC";

		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "s.";
				
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
