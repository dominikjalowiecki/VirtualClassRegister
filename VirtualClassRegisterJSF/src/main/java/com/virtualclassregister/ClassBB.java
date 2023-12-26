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
	
	@Inject
	Flash flash;
	
	@Getter private Class clazz;
	
	@Inject
	@Getter private LazyClass lazyClass;
	
	@Getter @Setter private String searchName;
	@Getter @Setter private int searchIdTutor;
	
	public ClassBB() {
		clazz = new Class();
		clazz.setUser(new User());
	}
	
	public List<Class> getList(){		
		return classDAO.getFullList();
	}
	
	public void addClass() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("name", clazz.getName());
		List<Class> clazzs = classDAO.getList(searchParams);
		
		if(!clazzs.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("name_is_already_used"), null));
			return;
		}
		
		classDAO.create(clazz);
		
		clazz = new Class();
		clazz.setUser(new User());
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_added_new_class"), null));
	}
	
	public String editClass(Class clazz) {
		flash.put("clazz", clazz);
		
		return "editClass?faces-redirect=true";
	}
	
	public void removeClass(Class clazz) {
		try {
			classDAO.remove(clazz);
		} catch(Exception e) {
			e.printStackTrace();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("unable_to_remove_class"), null));
			return;
		}
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_removed_class"), null));
	}
	
	public void search() {
		Map<String, Object> searchParams = new HashMap<>();
		if(!searchName.isEmpty()) {
			searchParams.put("like name", searchName);
		}
		if(searchIdTutor != 0) {
			User tutor = new User();
			tutor.setIdUser(searchIdTutor);
			searchParams.put("user", tutor);
		}
		lazyClass.setSearchParams(searchParams);
	}
	
	public void clear() {
		searchName = "";
		lazyClass.setSearchParams(null);
	}
	
}
