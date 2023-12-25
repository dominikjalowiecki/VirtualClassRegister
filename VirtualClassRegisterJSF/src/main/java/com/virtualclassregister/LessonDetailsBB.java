package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.virtualclassregister.dao.AnnouncementDAO;
import com.virtualclassregister.dao.LessonDAO;
import com.virtualclassregister.entities.Announcement;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

@Named
@ViewScoped
public class LessonDetailsBB implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	LessonDAO lessonDAO;
	
	@EJB
	AnnouncementDAO announcementDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	private Lesson loaded;
	
	@Getter private Lesson lesson;
	@Getter private Announcement announcement;
	
	public List<Announcement> getAnnouncements() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("lesson", lesson);
		
		return announcementDAO.getList(searchParams);
	}
	
	public void onLoad() throws IOException {
		loaded = (Lesson) flash.get("lesson");

		if (loaded != null) {
			lesson = new Lesson(loaded);
			announcement = new Announcement();
			announcement.setLesson(lesson);
		} else {
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid operation!", null));
			if (!ctx.isPostback()) {
				ctx.getExternalContext().redirect("teacherPanel.jsf");
				ctx.responseComplete();
			}
		}

	}
	
	public String studentDetails(User user, Lesson lesson) {
		flash.put("student", user);
		flash.put("lesson", lesson);
		
		return "studentDetails?faces-redirect=true";
	}
	
	public void addAnnouncement() {
		announcementDAO.create(announcement);
		announcement = new Announcement();
		announcement.setLesson(lesson);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully added new announcement", null));
	}
	
	public void removeAnnouncement(Announcement announcement) {
		announcementDAO.remove(announcement);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully removed announcement", null));
	}
	
}
