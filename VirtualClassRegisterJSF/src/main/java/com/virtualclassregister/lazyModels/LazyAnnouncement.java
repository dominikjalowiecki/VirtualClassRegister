package com.virtualclassregister.lazyModels;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.virtualclassregister.dao.AnnouncementDAO;
import com.virtualclassregister.entities.Announcement;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Setter;

@Named
@RequestScoped
public class LazyAnnouncement extends LazyDataModel<Announcement> {
	
	private static final long serialVersionUID = 1L;
	
	@Setter private Class clazz;
	@Setter private Semester semester;
	@Setter private Lesson lesson;
	
	@EJB
	AnnouncementDAO announcementDAO;

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		return announcementDAO.getCountByLesson(clazz, semester, lesson);
	}

	@Override
	public List<Announcement> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		return announcementDAO.getListByLesson(clazz, semester, lesson, offset, pageSize);
	}

}
