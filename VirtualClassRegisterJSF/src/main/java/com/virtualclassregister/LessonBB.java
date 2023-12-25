package com.virtualclassregister;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.virtualclassregister.dao.LessonDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;
import com.virtualclassregister.entities.Teacherteachessubject;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class LessonBB {
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;
	
	@EJB
	LessonDAO lessonDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	@Getter private Lesson lesson;
	
	@Getter @Setter private Date start;
	
	@Getter @Setter private Date end;
	
	
	public LessonBB() {
		lesson = new Lesson();
		lesson.setSemester(new Semester());
		lesson.setClazz(new Class());
		lesson.setTeacherteachessubject(new Teacherteachessubject());
	}
	
	public List<Lesson> getList(){		
		return lessonDAO.getFullList();
	}

	public void addLesson() {
		if(!start.before(end)) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "End time has to be before start time!", null));
			return;
		}
		
		lesson.setStart(new Time(start.getTime()));
		lesson.setEnd(new Time(end.getTime()));
		
		lessonDAO.create(lesson);
		
		lesson = new Lesson();
		lesson.setSemester(new Semester());
		lesson.setClazz(new Class());
		lesson.setTeacherteachessubject(new Teacherteachessubject());
		start = null;
		end = null;
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_added_new_lesson"), null));
	}
	
	public String editLesson(Lesson lesson) {
		flash.put("lesson", lesson);
		
		return "editLesson?faces-redirect=true";
	}
	
	public String removeLesson(Lesson lesson) {
		try {
			lessonDAO.remove(lesson);
		} catch(Exception e) {
			e.printStackTrace();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to remove lesson", null));
			return null;
		}
		
		ctx.getExternalContext().getFlash().setKeepMessages(true);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully removed lesson", null));
		return "class?faces-redirect=true";
	}
	
}
