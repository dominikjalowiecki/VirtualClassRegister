package com.virtualclassregister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.virtualclassregister.dao.ClassDAO;
import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.User;
import com.virtualclassregister.entities.Class;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class UserBB {
	
	@EJB
	UserDAO userDAO;
	
	@EJB
	ClassDAO classDAO;
	
	@Inject
	FacesContext ctx;
	
	private User user = new User();
	private String currentPassword;
	private int idClass;
	
	public User getUser() {
		return user;
	}
	
	public String getCurrentPassword() {
		return currentPassword;
	}
	
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword; 
	}
	
	public int getIdClass() {
		return idClass;
	}

	public void setIdClass(int idClass) {
		this.idClass = idClass;
	}
	
	public List<User> getTutors() {
		Map<String, String> searchParams = new HashMap<>();
		searchParams.put("role", "TEACHER");		
		return userDAO.getList(searchParams);
	}
	
	public void addUser() {
		Map<String, String> searchParams = new HashMap<>();
		searchParams.put("email", user.getEmail());
		List<User> users = userDAO.getList(searchParams);
		
		if(!users.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is already used!", null));
			user.setEmail(null);
			return;
		}
		
		Class clazz = classDAO.find(idClass);
		user.setClazz(clazz);
		user.setPassword( BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)) );
		userDAO.create(user);
		
		idClass = 0;
		user = new User();
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully added new user", null));
	}
	
	public void changePassword() {
//		BCrypt.checkpw(password, hash);
	}
	
}
