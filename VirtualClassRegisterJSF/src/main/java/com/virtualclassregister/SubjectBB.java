package com.virtualclassregister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;

import com.virtualclassregister.dao.SubjectDAO;
import com.virtualclassregister.dao.TeacherteachessubjectDAO;
import com.virtualclassregister.entities.Subject;
import com.virtualclassregister.entities.Teacherteachessubject;
import com.virtualclassregister.entities.User;
import com.virtualclassregister.lazyModels.LazySubject;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

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
		
	@Getter @Setter private Subject subject;
	
	@Getter @Setter private List<Integer> teachers;
	
	@Inject
	@Getter private LazySubject lazySubject;
	
	@Getter @Setter private String searchName;
	
	public SubjectBB() {
		subject = new Subject();
	}
	
	public void addSubject() {
		Map<String, Object> searchParams = new HashMap<>();
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
		Map<String, Object> searchParams = new HashMap<>();
		if(!searchName.isEmpty()) {
			searchParams.put("like name", searchName);
		}
		lazySubject.setSearchParams(searchParams);
	}
	
	public void clear() {
		searchName = "";
		lazySubject.setSearchParams(null);
	}
	
}
