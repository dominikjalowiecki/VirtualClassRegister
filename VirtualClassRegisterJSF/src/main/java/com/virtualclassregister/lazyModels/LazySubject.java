package com.virtualclassregister.lazyModels;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.virtualclassregister.dao.SubjectDAO;
import com.virtualclassregister.entities.Subject;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Setter;

@Named
@RequestScoped
public class LazySubject extends LazyDataModel<Subject> {
	
	private static final long serialVersionUID = 1L;
	
	@Setter private Map<String, Object> searchParams;
	
	@EJB
	SubjectDAO subjectDAO;

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		return subjectDAO.getCount(searchParams);
	}

	@Override
	public List<Subject> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		return subjectDAO.getList(searchParams, offset, pageSize);
	}

}
