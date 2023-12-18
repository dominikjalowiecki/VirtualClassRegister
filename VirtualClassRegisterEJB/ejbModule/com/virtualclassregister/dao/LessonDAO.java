package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;
import com.virtualclassregister.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class LessonDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Lesson lesson) {
		em.persist(lesson);
	}

	public Lesson merge(Lesson lesson) {
		return em.merge(lesson);
	}

	public void remove(Lesson lesson) {
		em.remove(em.merge(lesson));
	}

	public Lesson find(Object id) {
		return em.find(Lesson.class, id);
	}

	public List<Lesson> getFullList() {
		List<Lesson> list = null;

		Query query = em.createQuery("SELECT l FROM Lesson l");

		list = query.getResultList();

		return list;
	}
	
	public List<Lesson> getListByTeacher(Semester semester, User teacher) {
		List<Lesson> list = null;
		
		Query query = em.createQuery("SELECT l FROM Lesson l JOIN l.teacherteachessubject AS t WHERE l.semester = :semester AND t.user = :teacher");
		
		query.setParameter("semester", semester);
		query.setParameter("teacher", teacher);
		
		list = query.getResultList();
		
		return list;
	}
	
	public List<Lesson> getList(Map<String, Object> searchParams) {		
		List<Lesson> list = null;

		Query query = prepareQuery(searchParams);

		list = query.getResultList();

		return list;
	}
	
	private Query prepareQuery(Map<String, Object> searchParams) {
		String select = "SELECT l ";
		String from = "FROM Lesson l ";
		String where = "";

		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "l.";
				
				String words[] = key.split("\\s+");
				
				if(words.length == 2 && words[0].equals("like")) {
					where += words[1] + " LIKE :" + words[1] + " ";
				} else {
					where += key + " = :" + key + " ";
				}
			}
		}
		
		Query query = em.createQuery(select + from + where);
		
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
