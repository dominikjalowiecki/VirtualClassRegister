package com.virtualclassregister;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.simplesecurity.RemoteClient;

import org.mindrot.jbcrypt.BCrypt;

import com.virtualclassregister.dao.ClassDAO;
import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.User;
import com.virtualclassregister.lazyModels.LazyUser;

import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class UserBB implements Serializable {
	
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
	
	@Getter private User user;
	@Getter @Setter private String currentPassword;
	
	@Inject
	@Getter private LazyUser lazyUser;
	
	@Getter @Setter private String searchEmail;
	
	public UserBB() {
		user = new User();
		user.setClazz(new Class());
	}
	
	public List<User> getTutors() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("role", "Nauczyciel");		
		return userDAO.getList(searchParams);
	}
	
	public List<Class> getClasses(){
		if("Student".equals(user.getRole())) {
			return classDAO.getFullList();
		}
		return null;
	}
	
	public void addUser() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("email", user.getEmail());
		List<User> users = userDAO.getList(searchParams);
		
		if(!users.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("email_is_already_used"), null));
			user.setEmail(null);
			return;
		}

		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
		
		if(user.getClazz().getIdClass() == 0) {
			user.setClazz(null);
		}
		userDAO.create(user);
		
		user = new User();
		user.setClazz(new Class());
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_added_new_user"), null));
	}
	
	public void changePassword() {
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		RemoteClient<User> remoteClient = RemoteClient.load(session);
		User requestedUser = remoteClient.getDetails();
		
		if(!BCrypt.checkpw(currentPassword, requestedUser.getPassword())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("entered_password_doesnt_match_current_password"), null));
			return;
		}
		
		requestedUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
		userDAO.merge(requestedUser);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("password_has_been_successfully_changed"), null));
	}
	
	public String login() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("email", user.getEmail());		
		List<User> users = userDAO.getList(searchParams);
		
		if(users.isEmpty() || !BCrypt.checkpw(user.getPassword(), users.get(0).getPassword())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("provided_invalid_credentials"), null));
			return null;
		}
		
		User requestedUser = users.get(0);
			
		RemoteClient<User> client = new RemoteClient<User>();
		client.setDetails(requestedUser);
		
		client.getRoles().add(requestedUser.getRole());
	
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		client.store(request);
		
		return "profile?faces-redirect=true";
	}
	
	public String logout(){
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		session.invalidate();
		ctx.getExternalContext().getFlash().setKeepMessages(true);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("you_have_been_logged_out"), null));
		return "login?faces-redirect=true";
	}
	
	public String editUser(User user) {
		flash.put("user", user);
		
		return "editUser?faces-redirect=true";
	}
	
	public void removeUser(User user) {
		try {
			userDAO.remove(user);
		} catch(Exception e) {
			e.printStackTrace();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("unable_to_remove_user"), null));
			return;
		}
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_removed_user"), null));
	}
	
	public void search() {
		Map<String, Object> searchParams = new HashMap<>();
		if(!searchEmail.isEmpty()) {
			searchParams.put("like email", searchEmail);
		}
		lazyUser.setSearchParams(searchParams);
	}
	
	public void clear() {
		searchEmail = "";
		lazyUser.setSearchParams(null);
	}
	
}
