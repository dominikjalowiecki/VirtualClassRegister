package com.virtualclassregister.lazyModels;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.virtualclassregister.dao.ClassDAO;
import com.virtualclassregister.entities.Class;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Setter;

@Named
@RequestScoped
public class LazyClass extends LazyDataModel<Class> {
	
	private static final long serialVersionUID = 1L;
	
	@Setter private Map<String, Object> searchParams;
	
	@EJB
	ClassDAO classDAO;

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		return classDAO.getCount(searchParams);
	}

	@Override
	public List<Class> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		return classDAO.getList(searchParams, offset, pageSize);
	}

}
