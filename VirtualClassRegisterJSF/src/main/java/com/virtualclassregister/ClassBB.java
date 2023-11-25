package com.virtualclassregister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.virtualclassregister.dao.ClassDAO;
import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

@Named
@RequestScoped
public class ClassBB {
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;
	
	@EJB
	ClassDAO classDAO;
	
	@EJB
	UserDAO userDAO;
	
	@Inject
	FacesContext ctx;
	
	@Getter private Class clazz;
	
	public ClassBB() {
		clazz = new Class();
		clazz.setUser(new User());
	}
	
	public List<Class> getList(){		
		return classDAO.getFullList();
	}
	
	public void addClass() {
		Map<String, String> searchParams = new HashMap<>();
		searchParams.put("name", clazz.getName());
		List<Class> clazzs = classDAO.getList(searchParams);
		
		if(!clazzs.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("name_is_already_user"), null));
			clazz.setName(null);
			return;
		}
		
		classDAO.create(clazz);
		
		clazz = new Class();
		clazz.setUser(new User());
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_added_new_class"), null));
	}
	
}
