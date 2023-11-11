package com.virtualclassregister;

import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

import com.virtualclassregister.dao.SubjectDAO;
import com.virtualclassregister.entities.Subject;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class SubjectBB {
	
	@EJB
	SubjectDAO subjectDAO;
		
	public List<Subject> getList(){
		return subjectDAO.getFullList();
	}
	
	
}
