package com.virtualclassregister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.virtualclassregister.dao.ClassDAO;
import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class ClassBB {
	
	@EJB
	ClassDAO classDAO;
	
	@EJB
	UserDAO userDAO;
	
	@Inject
	FacesContext ctx;
	
	private Class clazz = new Class();
	private int idUser;
	
	public Class getClazz() {
		return clazz;
	}
	
	public List<Class> getList(){		
		return classDAO.getFullList();
	}
	
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
	public void addClass() {
		Map<String, String> searchParams = new HashMap<>();
		searchParams.put("name", clazz.getName());
		List<Class> clazzs = classDAO.getList(searchParams);
		
		if(!clazzs.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is already used!", null));
			clazz.setName(null);
			return;
		}
		
		User user = userDAO.find(idUser);
		clazz.setUser(user);
		classDAO.create(clazz);
		
		idUser = 0;
		clazz = new Class();
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully added new class", null));
	}
	
}
