package com.virtualclassregister.dao;

import java.util.List;

import com.virtualclassregister.entities.Lesson;

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

}
