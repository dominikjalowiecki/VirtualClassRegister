package com.virtualclassregister.converters;

import com.virtualclassregister.utils.Language;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "languageConverter")
public class LanguageConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext,
			UIComponent component, String value) {
		Language selectedLanguage = Language.availableLanguages.get(0);
		for(Language language : Language.availableLanguages) {
			if(language.getCode().equals(value)) {
				selectedLanguage = language;
				break;
			}
		} 
		return selectedLanguage;
	}

	@Override
	public String getAsString(FacesContext facesContext, 
			UIComponent component, Object value) {
		return value.toString();
	}

}
