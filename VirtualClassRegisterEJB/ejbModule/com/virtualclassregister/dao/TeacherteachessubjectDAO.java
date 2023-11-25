package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.Teacherteachessubject;

@Stateless
public class TeacherteachessubjectDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Teacherteachessubject teacherteachessubject) {
		em.persist(teacherteachessubject);
	}

	public Teacherteachessubject merge(Teacherteachessubject teacherteachessubject) {
		return em.merge(teacherteachessubject);
	}

	public void remove(Teacherteachessubject teacherteachessubject) {
		em.remove(em.merge(teacherteachessubject));
	}

	public Teacherteachessubject find(Object id) {
		return em.find(Teacherteachessubject.class, id);
	}

	public List<Teacherteachessubject> getFullList() {
		List<Teacherteachessubject> list = null;

		Query query = em.createQuery("SELECT t FROM Teacherteachessubject t");

		list = query.getResultList();

		return list;
	}
	
	public List<Teacherteachessubject> getList(Map<String, Object> searchParams) {
		List<Teacherteachessubject> list = null;
		
		Query query = prepareQuery(searchParams);

		list = query.getResultList();

		return list;
	}
	
	private Query prepareQuery(Map<String, Object> searchParams) {
		String select = "SELECT t ";
		String from = "FROM Teacherteachessubject t ";
		String where = "";
		String orderby = "ORDER BY t.idTeacherTeachesSubject ASC";

		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "t.";
				
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
