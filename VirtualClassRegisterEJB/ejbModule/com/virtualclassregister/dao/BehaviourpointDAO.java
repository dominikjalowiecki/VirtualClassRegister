package com.virtualclassregister.dao;

import java.util.List;

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

}
