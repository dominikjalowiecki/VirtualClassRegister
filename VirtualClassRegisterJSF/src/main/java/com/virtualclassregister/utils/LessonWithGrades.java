package com.virtualclassregister.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.virtualclassregister.entities.Grade;

import lombok.Getter;
import lombok.Setter;

public class LessonWithGrades {
	@Getter @Setter private String subjectName;
	@Getter @Setter private String teacherName;
	@Getter @Setter private List<Grade> grades;
	@Getter @Setter private BigDecimal weightedAverage;
	
	public LessonWithGrades(String subjectName, String teacherName) {
		this.subjectName = subjectName;
		this.teacherName = teacherName;
		grades = new ArrayList<>();
		weightedAverage = new BigDecimal("0");
	}
}
