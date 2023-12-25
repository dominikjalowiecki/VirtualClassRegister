package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import com.virtualclassregister.dao.LessonDAO;
import com.virtualclassregister.entities.Lesson;

import jakarta.ejb.EJB;
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
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid operation!", null));
			if (!ctx.isPostback()) {
				ctx.getExternalContext().redirect("class.jsf");
				ctx.responseComplete();
			}
		}

	}
	
	public void editLesson() {
		lesson.setStart(new Time(start.getTime()));
		lesson.setEnd(new Time(end.getTime()));
		
		lessonDAO.merge(lesson);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully updated class", null));
	}
	
	public String editClass() {
		flash.put("clazz", lesson.getClazz());
		
		return "editClass?faces-redirect=true";
	}
	
}
