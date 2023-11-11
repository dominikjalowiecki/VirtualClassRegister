package com.virtualclassregister.dao;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.virtualclassregister.entities.Announcement;

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

}
