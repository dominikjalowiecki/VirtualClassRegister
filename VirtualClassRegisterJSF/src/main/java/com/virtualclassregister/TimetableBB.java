package com.virtualclassregister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.virtualclassregister.dao.ClassDAO;
import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.User;
import com.virtualclassregister.lazyModels.LazyClass;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class TimetableBB {
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;
	
	@Inject
	FacesContext ctx;
	
	@Getter @Setter private int searchIdSemester;
	@Getter @Setter private int searchIdClass;
	@Getter @Setter private int searchIdTeacher;
	
	public void search() {}
	
}
