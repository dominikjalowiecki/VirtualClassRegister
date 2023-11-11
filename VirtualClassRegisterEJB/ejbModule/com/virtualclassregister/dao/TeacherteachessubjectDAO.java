package com.virtualclassregister.dao;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

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

}
