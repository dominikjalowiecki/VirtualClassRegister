package com.virtualclassregister;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.simplesecurity.RemoteClient;

import com.virtualclassregister.dao.LessonDAO;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class TeacherPanelBB implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;
	
	@EJB
	LessonDAO lessonDAO;
	
	@Inject
	Flash flash;
	
	@Inject
	FacesContext ctx;
	
	@Getter private List<Lesson> lessons;
	
	@Getter @Setter private int searchIdSemester;
	
	public TeacherPanelBB() {
		lessons = new ArrayList<>();
	}
	
	public String lessonDetails(Lesson lesson) {
		flash.put("lesson", lesson);
		
		return "lessonDetails?faces-redirect=true";
	}
	
	public void search() {
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		RemoteClient<User> remoteClient = RemoteClient.load(session);
		User requestedUser = remoteClient.getDetails();
		
		Semester semester = new Semester();
		semester.setIdSemester(searchIdSemester);
					
		lessons = lessonDAO.getListByTeacher(semester, requestedUser);
	}
	
}
