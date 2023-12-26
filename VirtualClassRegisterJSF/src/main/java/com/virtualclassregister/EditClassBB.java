package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.virtualclassregister.dao.ClassDAO;
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

@Named
@ViewScoped
public class EditClassBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;

	@EJB
	ClassDAO classDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	private Class loaded;
	
	@Getter private Class clazz;
	
	public void onLoad() throws IOException {
		loaded = (Class) flash.get("clazz");

		if (loaded != null) {
			clazz = new Class(loaded);
		} else {
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("invalid_operation"), null));
			if (!ctx.isPostback()) {
				ctx.getExternalContext().redirect("class.jsf");
				ctx.responseComplete();
			}
		}
	}
	
	public void editClass() {
		if(!clazz.getName().equals(loaded.getName())) {
			Map<String, Object> searchParams = new HashMap<>();
			searchParams.put("name", clazz.getName());
			List<Class> clazzes = classDAO.getList(searchParams);
			
			if(!clazzes.isEmpty()) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("name_is_already_used"), null));
				return;
			}
		}
		
		classDAO.merge(clazz);
		
		loaded = new Class(clazz);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, textMessage.getString("successfully_updated_class"), null));
	}
	
	public String gradeBook(User student) {
		flash.put("student", student);
		
		return "gradeBook?faces-redirect=true";
	}
	
}
