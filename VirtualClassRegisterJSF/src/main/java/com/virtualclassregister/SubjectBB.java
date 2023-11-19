package com.virtualclassregister;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.virtualclassregister.dao.SubjectDAO;
import com.virtualclassregister.dao.TeacherteachessubjectDAO;
import com.virtualclassregister.entities.Subject;
import com.virtualclassregister.entities.Teacherteachessubject;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class SubjectBB {
	
	@EJB
	SubjectDAO subjectDAO;
	
	@EJB
	TeacherteachessubjectDAO teacherteachessubjectDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	private Map<String, String> searchParams;
	
	private Subject subject;
	
    private List<Integer> teachers;

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public List<Subject> getSubjects(){
		return subjectDAO.getList(searchParams);
	}
	
	public List<Integer> getTeachers() {
		return teachers;
	}
	
	public void setTeachers(List<Integer> teachers) {
		this.teachers = teachers;
	}
	
	
	public SubjectBB() {
		subject = new Subject();
		searchParams = new HashMap<>();
	}
	
	public void addSubject() {
		Map<String, String> searchParams = new HashMap<>();
		searchParams.put("name", subject.getName());
		List<Subject> subjects = subjectDAO.getList(searchParams);
		
		if(!subjects.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is already used!", null));
			return;
		}
		
		subjectDAO.create(subject);
		
		for(Integer idTeacher : teachers) {
			Teacherteachessubject teacherteachessubject = new Teacherteachessubject();
			teacherteachessubject.setSubject(subject);
			User teacher = new User();
			teacher.setIdUser(idTeacher);
			teacherteachessubject.setUser(teacher);
			teacherteachessubjectDAO.create(teacherteachessubject);
		}
		
		subject = new Subject();
		teachers = null;
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully added new subject", null));
	}
	
	public String editSubject(Subject subject) {
		flash.put("subject", subject);
		
		return "editSubject?faces-redirect=true";
	}
	
	public void removeSubject(Subject subject) {
		subjectDAO.remove(subject);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully removed subject", null));
	}
	
	public void search() {
		Map<String, String> searchParams = new HashMap<>();
		if(!subject.getName().isEmpty()) {
			searchParams.put("name", subject.getName());
		}
		this.searchParams = searchParams;
	}
	
}
