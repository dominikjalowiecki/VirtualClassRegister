package com.virtualclassregister.lazyModels;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.virtualclassregister.dao.UserDAO;
import com.virtualclassregister.entities.User;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Setter;

@Named
@RequestScoped
public class LazyUser extends LazyDataModel<User> {
	
	private static final long serialVersionUID = 1L;
	
	@Setter private Map<String, Object> searchParams;
	
	@EJB
	UserDAO userDAO;

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		return userDAO.getCount(searchParams);
	}

	@Override
	public List<User> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		return userDAO.getList(searchParams, offset, pageSize);
	}

}
