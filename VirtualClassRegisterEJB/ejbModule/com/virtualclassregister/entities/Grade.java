package com.virtualclassregister.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the grade database table.
 * 
 */
@Entity
@NamedQuery(name="Grade.findAll", query="SELECT g FROM Grade g")
public class Grade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idGrade;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false, updatable = false)
	private Date created;

	@Lob
	private String note;

	private BigDecimal value;

	//bi-directional many-to-one association to Grade
	@ManyToOne
	@JoinColumn(name="`re-approach_idGrade`")
	private Grade grade;

	//bi-directional many-to-one association to Grade
	@OneToMany(mappedBy="grade")
	private List<Grade> grades;

	//bi-directional many-to-one association to Gradetype
	@ManyToOne
	@JoinColumn(name="idGradeType")
	private Gradetype gradetype;

	//bi-directional many-to-one association to Lesson
	@ManyToOne
	@JoinColumn(name="idLesson")
	private Lesson lesson;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="student_idUser")
	private User user;

	public Grade() {
	}

	public int getIdGrade() {
		return this.idGrade;
	}

	public void setIdGrade(int idGrade) {
		this.idGrade = idGrade;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Grade getGrade() {
		return this.grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public List<Grade> getGrades() {
		return this.grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public Grade addGrade(Grade grade) {
		getGrades().add(grade);
		grade.setGrade(this);

		return grade;
	}

	public Grade removeGrade(Grade grade) {
		getGrades().remove(grade);
		grade.setGrade(null);

		return grade;
	}

	public Gradetype getGradetype() {
		return this.gradetype;
	}

	public void setGradetype(Gradetype gradetype) {
		this.gradetype = gradetype;
	}

	public Lesson getLesson() {
		return this.lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}