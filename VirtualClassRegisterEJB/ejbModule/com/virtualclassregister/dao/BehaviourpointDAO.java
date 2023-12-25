package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import com.virtualclassregister.entities.Behaviourpoint;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class BehaviourpointDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Behaviourpoint behaviourpoint) {
		em.persist(behaviourpoint);
	}

	public Behaviourpoint merge(Behaviourpoint behaviourpoint) {
		return em.merge(behaviourpoint);
	}

	public void remove(Behaviourpoint behaviourpoint) {
		em.remove(em.merge(behaviourpoint));
	}

	public Behaviourpoint find(Object id) {
		return em.find(Behaviourpoint.class, id);
	}

	public List<Behaviourpoint> getFullList() {
		List<Behaviourpoint> list = null;

		Query query = em.createQuery("SELECT b FROM Behaviourpoint b");

		list = query.getResultList();

		return list;
	}
	
	public List<Behaviourpoint> getList(Map<String, Object> searchParams) {		
		List<Behaviourpoint> list = null;

		Query query = prepareQuery(searchParams);

		list = query.getResultList();

		return list;
	}
	
	private Query prepareQuery(Map<String, Object> searchParams) {
		String select = "SELECT b ";
		String from = "FROM Behaviourpoint b ";
		String where = "";
		String orderby = "ORDER BY b.created ASC";

		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "b.";
				
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
