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
import lombok.Getter;
import lombok.Setter;

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
	
	@Getter private Subject subject;
		
	@Getter @Setter private List<Integer> teachers;
	
	public EditSubjectBB() {
		teachers = new ArrayList<>();
	}
	
	public void onLoad() throws IOException {
		loaded = (Subject) flash.get("subject");

		if (loaded != null) {
			subject = new Subject(loaded);
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
	
	public void editSubject() {
		if(!subject.getName().equals(loaded.getName())) {
			Map<String, Object> searchParams = new HashMap<>();
			searchParams.put("name", subject.getName());
			List<Subject> subjects = subjectDAO.getList(searchParams);
			
			if(!subjects.isEmpty()) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is already used!", null));
				return;
			}
		}
		
		subjectDAO.merge(subject);
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("subject", subject);
		List<Teacherteachessubject> teacherteachessubjects = teacherteachessubjectDAO.getList(searchParams);
		List<Integer> idsTeachers = new ArrayList<>();
		
		for(Teacherteachessubject teacherteachessubject : teacherteachessubjects) {
			Integer idTeacher = teacherteachessubject.getUser().getIdUser();
			idsTeachers.add(idTeacher);
			if(!teachers.contains(idTeacher)) {
				try {
					teacherteachessubjectDAO.remove(teacherteachessubject);
				} catch(Exception e) {
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to remove teacher from subject", null));
					e.printStackTrace();
				}
			}
		}
		
		for(Integer idTeacher : teachers) {
			if(!idsTeachers.contains(idTeacher)) {
				User teacher = new User();
				teacher.setIdUser(idTeacher);
				
				Teacherteachessubject teacherteachessubject = new Teacherteachessubject();
				teacherteachessubject.setSubject(subject);
				teacherteachessubject.setUser(teacher);
				
				teacherteachessubjectDAO.create(teacherteachessubject);
			}
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully updated subject", null));
	}
	
}
