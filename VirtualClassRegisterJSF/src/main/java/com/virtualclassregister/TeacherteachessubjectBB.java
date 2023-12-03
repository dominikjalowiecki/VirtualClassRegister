package com.virtualclassregister;

import java.util.List;
import java.util.ResourceBundle;

import com.virtualclassregister.dao.TeacherteachessubjectDAO;
import com.virtualclassregister.entities.Teacherteachessubject;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class TeacherteachessubjectBB {
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;
	
	@EJB
	TeacherteachessubjectDAO teacherteachessubjectDAO;

	
	public List<Teacherteachessubject> getList(){		
		return teacherteachessubjectDAO.getFullList();
	}
	
}
