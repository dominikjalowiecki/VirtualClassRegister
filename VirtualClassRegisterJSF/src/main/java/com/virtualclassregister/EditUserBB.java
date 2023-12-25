package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.virtualclassregister.dao.ClassDAO;
import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
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
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid operation!", null));
			if (!ctx.isPostback()) {
				ctx.getExternalContext().redirect("user.jsf");
				ctx.responseComplete();
			}
		}

	}
	
	public List<Class> getClasses(){
		if("STUDENT".equals(user.getRole())) {
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
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is already used!", null));
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
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully updated user", null));
	}
	
}
