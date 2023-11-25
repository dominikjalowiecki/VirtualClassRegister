package com.virtualclassregister.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Language {
	public static final List<Language> availableLanguages = new ArrayList<>(
			List.of(
				new Language("English", "en"),
				new Language("Polski", "pl")
			)
		);

	@Getter private String name;
	@Getter private String code;
	
	public Language(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	@Override
	public String toString() {
		return this.code;
	}
}