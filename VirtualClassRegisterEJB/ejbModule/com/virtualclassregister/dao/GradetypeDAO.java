package com.virtualclassregister.dao;

import java.util.List;

import com.virtualclassregister.entities.Gradetype;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class GradetypeDAO {
	private final static String UNIT_NAME = "VirtualClassRegisterPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Gradetype gradetype) {
		em.persist(gradetype);
	}

	public Gradetype merge(Gradetype gradetype) {
		return em.merge(gradetype);
	}

	public void remove(Gradetype gradetype) {
		em.remove(em.merge(gradetype));
	}

	public Gradetype find(Object id) {
		return em.find(Gradetype.class, id);
	}

	public List<Gradetype> getFullList() {
		List<Gradetype> list = null;

		Query query = em.createQuery("SELECT g FROM Gradetype g");

		list = query.getResultList();

		return list;
	}

}
