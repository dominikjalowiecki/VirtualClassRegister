package com.virtualclassregister;

import java.util.List;

import com.virtualclassregister.dao.TeacherteachessubjectDAO;
import com.virtualclassregister.entities.Teacherteachessubject;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class TeacherteachessubjectBB {
	
	@EJB
	TeacherteachessubjectDAO teacherteachessubjectDAO;

	
	public List<Teacherteachessubject> getList(){		
		return teacherteachessubjectDAO.getFullList();
	}
	
}
