package com.virtualclassregister;

import java.io.Serializable;
import java.util.List;

import com.virtualclassregister.utils.Language;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class LangBB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter @Setter private Language selectedLanguage;
	@Getter private List<Language> availableLanguages;
	
	@Inject
	ExternalContext ectx;
	
	public LangBB() {
		availableLanguages = Language.availableLanguages;
	}
	
	@PostConstruct
	public void init() {
		String languageCode = ectx.getRequestLocale().getCountry().toLowerCase();
		selectedLanguage = Language.availableLanguages.get(0);
		for(Language language : Language.availableLanguages) {
			if(language.getCode().equals(languageCode)) {
				selectedLanguage = language;
				break;
			}
		}
	}

}