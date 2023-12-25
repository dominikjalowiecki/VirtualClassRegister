package com.virtualclassregister;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.simplesecurity.RemoteClient;

import com.virtualclassregister.dao.AnnouncementDAO;
import com.virtualclassregister.dao.LessonDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;
import com.virtualclassregister.entities.User;
import com.virtualclassregister.lazyModels.LazyAnnouncement;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class AnnouncementBB implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EJB
	AnnouncementDAO announcementDAO;
	
	@EJB
	LessonDAO lessonDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	@Getter private LazyAnnouncement lazyAnnouncement;
	
	@Getter @Setter private int searchIdSemester;
	@Getter @Setter private int searchIdLesson;
	private User requestedUser;
	
	public List<Lesson> getLessons() {
		if(!"STUDENT".equals(requestedUser.getRole())) {
			return null;
		}

		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("clazz", requestedUser.getClazz());
		
		Semester semester = null;
		if(searchIdSemester != 0) {
			semester = new Semester();
			semester.setIdSemester(searchIdSemester);
		}
		searchParams.put("semester", semester);
		
		return lessonDAO.getList(searchParams);
	}
	
	@PostConstruct
    public void init() {
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		RemoteClient<User> remoteClient = RemoteClient.load(session);
		requestedUser = remoteClient.getDetails();
		
		Class clazz = null;
		if(requestedUser.getRole() == "STUDENT") {
			clazz = requestedUser.getClazz();
		}
		lazyAnnouncement.setClazz(clazz);
		
		Semester semester = null;
		if(searchIdSemester != 0) {
			semester = new Semester();
			semester.setIdSemester(searchIdSemester);
		}
		lazyAnnouncement.setSemester(semester);
		
		Lesson lesson = null;
		if(searchIdLesson != 0) {
			lesson = new Lesson();
			lesson.setIdLesson(searchIdLesson);
		}
		lazyAnnouncement.setLesson(lesson);
	}
	
	public void search() {
		Class clazz = null;
		if(requestedUser.getRole() == "STUDENT") {
			clazz = requestedUser.getClazz();
		}
		lazyAnnouncement.setClazz(clazz);
		
		Semester semester = null;
		if(searchIdSemester != 0) {
			semester = new Semester();
			semester.setIdSemester(searchIdSemester);
		}
		lazyAnnouncement.setSemester(semester);
		
		Lesson lesson = null;
		if(searchIdLesson != 0) {
			lesson = new Lesson();
			lesson.setIdLesson(searchIdLesson);
		}

		lazyAnnouncement.setLesson(lesson);
	}
	
}
