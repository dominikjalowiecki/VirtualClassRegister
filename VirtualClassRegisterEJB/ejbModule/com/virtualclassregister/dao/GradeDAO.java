package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import com.virtualclassregister.entities.Grade;
import com.virtualclassregister.entities.Semester;
import com.virtualclassregister.entities.User;

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
	
	public List<Grade> getListByStudent(Semester semester, User student) {
		List<Grade> list = null;
		
		Query query = em.createQuery("SELECT g FROM Grade g JOIN g.lesson AS l WHERE l.semester = :semester AND g.user = :student ORDER BY l.idLesson ASC, g.created ASC");
		
		query.setParameter("semester", semester);
		query.setParameter("student", student);
		
		list = query.getResultList();
		
		return list;
	}
	
	public List<Grade> getList(Map<String, Object> searchParams) {		
		List<Grade> list = null;

		Query query = prepareQuery(searchParams);

		list = query.getResultList();

		return list;
	}
	
	private Query prepareQuery(Map<String, Object> searchParams) {
		String select = "SELECT g ";
		String from = "FROM Grade g ";
		String where = "";
		String orderby = "ORDER BY g.created ASC";

		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "g.";
				
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
