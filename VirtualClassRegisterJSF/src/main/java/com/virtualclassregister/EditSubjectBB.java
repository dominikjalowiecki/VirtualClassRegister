package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.virtualclassregister.dao.SubjectDAO;
import com.virtualclassregister.dao.TeacherteachessubjectDAO;
import com.virtualclassregister.entities.Subject;
import com.virtualclassregister.entities.Teacherteachessubject;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class EditSubjectBB implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	SubjectDAO subjectDAO;
	
	@EJB
	TeacherteachessubjectDAO teacherteachessubjectDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	private Subject loaded;
	
	private Subject subject;
		
	private List<Integer> teachers;

	public Subject getSubject() {
		return subject;
	}
	
	public List<Integer> getTeachers() {
		return teachers;
	}
	
	public void setTeachers(List<Integer> teachers) {
		this.teachers = teachers;
	}
	
	public void onLoad() throws IOException {
		loaded = (Subject) flash.get("subject");

		if (loaded != null) {
			subject = loaded;
			List<Teacherteachessubject> teacherteachessubjects = subject.getTeacherteachessubjects();
			for(Teacherteachessubject teacherteachessubject : teacherteachessubjects) {
				teachers.add(teacherteachessubject.getUser().getIdUser());
			}
		} else {
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid operation!", null));
			if (!ctx.isPostback()) {
				ctx.getExternalContext().redirect("subject.jsf");
				ctx.responseComplete();
			}
		}

	}
	
	public EditSubjectBB() {
		teachers = new ArrayList<>();
	}
	
	public void editSubject() {
		Map<String, String> searchParams = new HashMap<>();
		searchParams.put("name", subject.getName());
		List<Subject> subjects = subjectDAO.getList(searchParams);
		
		if(!subjects.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is already used!", null));
			return;
		}
		
		subjectDAO.merge(subject);
		
		for(Integer idTeacher : teachers) {
			Teacherteachessubject teacherteachessubject = new Teacherteachessubject();
			teacherteachessubject.setSubject(subject);
			User teacher = new User();
			teacher.setIdUser(idTeacher);
			teacherteachessubject.setUser(teacher);
			teacherteachessubjectDAO.create(teacherteachessubject);
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully updated subject", null));
	}
	
}
