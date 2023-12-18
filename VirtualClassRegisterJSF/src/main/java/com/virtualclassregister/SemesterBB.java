package com.virtualclassregister;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.virtualclassregister.dao.SemesterDAO;
import com.virtualclassregister.entities.Semester;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class SemesterBB {
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;
	
	@EJB
	SemesterDAO semesterDAO;
	
	@Inject
	FacesContext ctx;
	
	@Getter private Semester semester;
	
	@Getter @Setter private List<Date> range;
	
	public SemesterBB() {
		semester = new Semester();
	}
	
	public LocalDate getCurrentDate() {
		return LocalDate.now();
	}
	
	public List<Semester> getList(){		
		return semesterDAO.getFullList();
	}

	public void addSemester() {
		semester.setStart(range.get(0));
		semester.setEnd(range.get(1));
		
		semesterDAO.create(semester);
		
		semester = new Semester();
		range = null;
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_added_new_semester"), null));
	}
	
}
