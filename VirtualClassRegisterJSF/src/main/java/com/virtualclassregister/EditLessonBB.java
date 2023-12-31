package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;

import com.virtualclassregister.dao.LessonDAO;
import com.virtualclassregister.entities.Lesson;

import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EditLessonBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;

	@EJB
	LessonDAO lessonDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	private Lesson loaded;
	
	@Getter private Lesson lesson;
	
	@Getter @Setter private Date start;
	
	@Getter @Setter private Date end;
	
	public void onLoad() throws IOException {
		loaded = (Lesson) flash.get("lesson");

		if (loaded != null) {
			lesson = new Lesson(loaded);
			start = new Date(lesson.getStart().getTime());
			end = new Date(lesson.getEnd().getTime());
		} else {
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("invalid_operation"), null));
			if (!ctx.isPostback()) {
				ctx.getExternalContext().redirect("class.jsf");
				ctx.responseComplete();
			}
		}

	}
	
	public void editLesson() {
		if(!start.before(end)) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("end_time_has_to_be_before_start_time"), null));
			return;
		}
		
		lesson.setStart(new Time(start.getTime()));
		lesson.setEnd(new Time(end.getTime()));
		
		lessonDAO.merge(lesson);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_updated_lesson"), null));
	}
	
	public String editClass() {
		flash.put("clazz", lesson.getClazz());
		
		return "editClass?faces-redirect=true";
	}
	
}
