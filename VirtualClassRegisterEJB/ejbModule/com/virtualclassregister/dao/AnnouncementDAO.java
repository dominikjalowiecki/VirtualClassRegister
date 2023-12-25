package com.virtualclassregister.dao;

import java.util.List;
import java.util.Map;

import com.virtualclassregister.entities.Announcement;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class AnnouncementDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Announcement announcement) {
		em.persist(announcement);
	}

	public Announcement merge(Announcement announcement) {
		return em.merge(announcement);
	}

	public void remove(Announcement announcement) {
		em.remove(em.merge(announcement));
	}

	public Announcement find(Object id) {
		return em.find(Announcement.class, id);
	}

	public List<Announcement> getFullList() {
		List<Announcement> list = null;

		Query query = em.createQuery("SELECT a FROM Announcement a");

		list = query.getResultList();

		return list;
	}
	
	public int getCountByLesson(Class clazz, Semester semester, Lesson lesson) {
		String queryString = "SELECT COUNT(a) FROM Announcement a JOIN a.lesson AS l";
		
		if(clazz != null) {
			queryString += " WHERE l.clazz = :clazz";
		}
		
		if(semester != null) {
			if(clazz != null) {
				queryString += " AND";
			} else {
				queryString += " WHERE";
			}
			
			queryString += " l.semester = :semester";
		}
		
		if(lesson != null) {
			queryString += " AND a.lesson = :lesson";
		}
		
		queryString += " ORDER BY a.created DESC";
		
		Query query = em.createQuery(queryString);
		
		if(clazz != null) {
			query.setParameter("clazz", clazz);
		}
		
		if(semester != null) {
			query.setParameter("semester", semester);
		}
		
		if(lesson != null) {
			query.setParameter("lesson", lesson);
		}
		
		int count = ((Long) query.getSingleResult()).intValue();
		return count;
	}
	
	public List<Announcement> getListByLesson(Class clazz, Semester semester, Lesson lesson, int offset, int pageSize) {
		List<Announcement> list = null;
				
		String queryString = "SELECT a FROM Announcement a JOIN a.lesson AS l";
		
		if(clazz != null) {
			queryString += " WHERE l.clazz = :clazz";
		}
		
		if(semester != null) {
			if(clazz != null) {
				queryString += " AND";
			} else {
				queryString += " WHERE";
			}
			
			queryString += " l.semester = :semester";
		}
		
		if(lesson != null) {
			queryString += " AND a.lesson = :lesson";
		}
		
		queryString += " ORDER BY a.created DESC";
		
		Query query = em.createQuery(queryString);
		
		if(clazz != null) {
			query.setParameter("clazz", clazz);
		}
		
		if(semester != null) {
			query.setParameter("semester", semester);
		}
		
		if(lesson != null) {
			query.setParameter("lesson", lesson);
		}
		
		list = query
				.setMaxResults(pageSize)
				.setFirstResult(offset)
				.getResultList();
		
		return list;
	}
	
	public List<Announcement> getList(Map<String, Object> searchParams) {		
		List<Announcement> list = null;

		Query query = prepareQuery(searchParams);

		list = query.getResultList();

		return list;
	}
	
	private Query prepareQuery(Map<String, Object> searchParams) {
		String select = "SELECT a ";
		String from = "FROM Announcement a ";
		String where = "";
		String orderby = "ORDER BY a.created DESC";

		if(searchParams != null) {
			for (Map.Entry<String, Object> set : searchParams.entrySet()) {
				if (where.isEmpty()) {
					where = "WHERE ";
				} else {
					where += "AND ";
				}
				
				String key = set.getKey();
				
				where += "a.";
				
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
