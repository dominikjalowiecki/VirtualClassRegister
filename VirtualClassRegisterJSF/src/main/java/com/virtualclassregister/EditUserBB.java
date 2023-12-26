package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import com.virtualclassregister.dao.ClassDAO;
import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EditUserBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;

	@EJB
	UserDAO userDAO;
	
	@EJB
	ClassDAO classDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	private User loaded;
	
	@Getter private User user;
	
	@Getter @Setter private String newPassword;
	
	public void onLoad() throws IOException {
		loaded = (User) flash.get("user");

		if (loaded != null) {
			user = new User(loaded);
			if(user.getClazz() == null) {
				user.setClazz(new Class());
			}
		} else {
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("invalid_operation"), null));
			if (!ctx.isPostback()) {
				ctx.getExternalContext().redirect("user.jsf");
				ctx.responseComplete();
			}
		}

	}
	
	public List<Class> getClasses(){
		if("Student".equals(user.getRole())) {
			return classDAO.getFullList();
		}
		return null;
	}
	
	public void editUser() {
		if(!user.getEmail().equals(loaded.getEmail())) {
			Map<String, Object> searchParams = new HashMap<>();
			searchParams.put("email", user.getEmail());
			List<User> users = userDAO.getList(searchParams);
			
			if(!users.isEmpty()) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("email_is_already_used"), null));
				return;
			}
		}
		
		
		if(!newPassword.isEmpty()) {
			user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
		}
		
		if(user.getClazz().getIdClass() == 0) {
			user.setClazz(null);
		}	
		
		userDAO.merge(user);
		
		loaded = new User(user);
		user.setClazz(new Class());
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_updated_user"), null));
	}
	
}
