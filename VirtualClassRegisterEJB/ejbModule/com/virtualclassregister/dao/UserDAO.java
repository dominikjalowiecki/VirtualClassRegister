package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import com.virtualclassregister.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(User user) {
		em.persist(user);
	}

	public User merge(User user) {
		return em.merge(user);
	}

	public void remove(User user) {
		em.remove(em.merge(user));
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}

	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("SELECT u FROM User u ORDER BY u.forename, u.surname ASC");

		list = query.getResultList();

		return list;
	}
	
	public List<User> getList(Map<String, String> searchParams) {
		List<User> list = null;

		String select = "SELECT u ";
		String from = "FROM User u ";
		String where = "";
		String orderby = "ORDER BY u.forename, u.surname ASC";

		for (Map.Entry<String, String> set : searchParams.entrySet()) {
			if (where.isEmpty()) {
				where = "WHERE ";
			} else {
				where += "AND ";
			}
			where += "u." + set.getKey() + " = :" + set.getKey() + " ";
		}
		
		Query query = em.createQuery(select + from + where + orderby);
		
		for (Map.Entry<String, String> set : searchParams.entrySet()) {
			query.setParameter(set.getKey(), set.getValue());
		}

		list = query.getResultList();

		return list;
	}

}
