package com.virtualclassregister.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the gradetype database table.
 * 
 */
@Entity
@NamedQuery(name="Gradetype.findAll", query="SELECT g FROM Gradetype g")
public class Gradetype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idGradeType;

	private String name;

	private BigDecimal weightage;

	//bi-directional many-to-one association to Grade
	@OneToMany(mappedBy="gradetype")
	private List<Grade> grades;

	public Gradetype() {
	}

	public int getIdGradeType() {
		return this.idGradeType;
	}

	public void setIdGradeType(int idGradeType) {
		this.idGradeType = idGradeType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getWeightage() {
		return this.weightage;
	}

	public void setWeightage(BigDecimal weightage) {
		this.weightage = weightage;
	}

	public List<Grade> getGrades() {
		return this.grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public Grade addGrade(Grade grade) {
		getGrades().add(grade);
		grade.setGradetype(this);

		return grade;
	}

	public Grade removeGrade(Grade grade) {
		getGrades().remove(grade);
		grade.setGradetype(null);

		return grade;
	}

}