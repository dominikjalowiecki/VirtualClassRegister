package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.virtualclassregister.dao.BehaviourpointDAO;
import com.virtualclassregister.dao.GradeDAO;
import com.virtualclassregister.dao.GradetypeDAO;
import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.Behaviourpoint;
import com.virtualclassregister.entities.Grade;
import com.virtualclassregister.entities.Gradetype;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.User;

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
public class StudentDetailsBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;

	@EJB
	UserDAO userDAO;
	
	@EJB
	GradeDAO gradeDAO;
	
	@EJB
	GradetypeDAO gradetypeDAO;
	
	@EJB
	BehaviourpointDAO behaviourpointDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	private User loadedStudent;
	private Lesson loadedLesson;
	
	@Getter private User student;
	@Getter private Lesson lesson;
	
	@Getter @Setter private Integer idGradeType;
	@Getter @Setter private BigDecimal gradeValue;
	@Getter @Setter private String gradeNote;
	
	@Getter @Setter private Byte behaviourPointValue;
	@Getter @Setter private String behaviourPointNote;
	
	public List<Gradetype> getGradeTypes(){		
		return gradetypeDAO.getFullList();
	}
	
	public List<Grade> getGrades() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("user", student);
		searchParams.put("lesson", lesson);
		List<Grade> grades = gradeDAO.getList(searchParams);
		
		return grades;
	}
	
	public List<Behaviourpoint> getBehaviourPoints() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("user", student);
		List<Behaviourpoint> behaviourPoints = behaviourpointDAO.getList(searchParams);
		
		return behaviourPoints;
	}
	
	public void onLoad() throws IOException {
		loadedStudent = (User) flash.get("student");
		loadedLesson = (Lesson) flash.get("lesson");
		

		if (loadedStudent != null && loadedLesson != null) {
			student = new User(loadedStudent);
			lesson = new Lesson(loadedLesson);
		} else {
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid operation!", null));
			if (!ctx.isPostback()) {
				ctx.getExternalContext().redirect("teacherPanel.jsf");
				ctx.responseComplete();
			}
		}

	}
	
	public void addGrade() {
		Grade grade = new Grade();
		grade.setUser(student);
		grade.setLesson(lesson);
		
		Gradetype gradetype = new Gradetype();
		gradetype.setIdGradeType(idGradeType);
		grade.setGradetype(gradetype);
		grade.setValue(gradeValue);
		grade.setNote(gradeNote);
		
		gradeDAO.create(grade);
		
		idGradeType = null;
		gradeValue = null;
		gradeNote = null;
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_added_new_grade"), null));
	}
	
	public void addBehaviourPoints() {
		Behaviourpoint behaviourpoint = new Behaviourpoint();
		behaviourpoint.setUser(student);
		behaviourpoint.setClazz(student.getClazz());
		behaviourpoint.setSemester(lesson.getSemester());
		behaviourpoint.setValue(behaviourPointValue);
		behaviourpoint.setNote(behaviourPointNote);
		
		behaviourpointDAO.create(behaviourpoint);
		
		behaviourPointValue = null;
		behaviourPointNote = null;
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_added_new_behaviour_point"), null));
	}
		
	public String lessonDetails() {
		flash.put("lesson", lesson);
		
		return "lessonDetails?faces-redirect=true";
	}
	
}
