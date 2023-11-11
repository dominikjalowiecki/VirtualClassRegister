package com.virtualclassregister.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false, updatable = false)
	private Date created;

	private String email;

	private String forename;

	private String password;

	private String role;

	private String surname;

	//bi-directional many-to-one association to Behaviourpoint
	@OneToMany(mappedBy="user")
	private List<Behaviourpoint> behaviourpoints;

	//bi-directional many-to-one association to Class
	@OneToMany(mappedBy="user")
	private List<Class> clazzs;

	//bi-directional many-to-one association to Grade
	@OneToMany(mappedBy="user")
	private List<Grade> grades;

	//bi-directional many-to-one association to Teacherteachessubject
	@OneToMany(mappedBy="user")
	private List<Teacherteachessubject> teacherteachessubjects;

	//bi-directional many-to-one association to Class
	@ManyToOne
	@JoinColumn(name="idClass")
	private Class clazz;

	public User() {
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getForename() {
		return this.forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Behaviourpoint> getBehaviourpoints() {
		return this.behaviourpoints;
	}

	public void setBehaviourpoints(List<Behaviourpoint> behaviourpoints) {
		this.behaviourpoints = behaviourpoints;
	}

	public Behaviourpoint addBehaviourpoint(Behaviourpoint behaviourpoint) {
		getBehaviourpoints().add(behaviourpoint);
		behaviourpoint.setUser(this);

		return behaviourpoint;
	}

	public Behaviourpoint removeBehaviourpoint(Behaviourpoint behaviourpoint) {
		getBehaviourpoints().remove(behaviourpoint);
		behaviourpoint.setUser(null);

		return behaviourpoint;
	}

	public List<Class> getClazzs() {
		return this.clazzs;
	}

	public void setClazzs(List<Class> clazzs) {
		this.clazzs = clazzs;
	}

	public Class addClazz(Class clazz) {
		getClazzs().add(clazz);
		clazz.setUser(this);

		return clazz;
	}

	public Class removeClazz(Class clazz) {
		getClazzs().remove(clazz);
		clazz.setUser(null);

		return clazz;
	}

	public List<Grade> getGrades() {
		return this.grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public Grade addGrade(Grade grade) {
		getGrades().add(grade);
		grade.setUser(this);

		return grade;
	}

	public Grade removeGrade(Grade grade) {
		getGrades().remove(grade);
		grade.setUser(null);

		return grade;
	}

	public List<Teacherteachessubject> getTeacherteachessubjects() {
		return this.teacherteachessubjects;
	}

	public void setTeacherteachessubjects(List<Teacherteachessubject> teacherteachessubjects) {
		this.teacherteachessubjects = teacherteachessubjects;
	}

	public Teacherteachessubject addTeacherteachessubject(Teacherteachessubject teacherteachessubject) {
		getTeacherteachessubjects().add(teacherteachessubject);
		teacherteachessubject.setUser(this);

		return teacherteachessubject;
	}

	public Teacherteachessubject removeTeacherteachessubject(Teacherteachessubject teacherteachessubject) {
		getTeacherteachessubjects().remove(teacherteachessubject);
		teacherteachessubject.setUser(null);

		return teacherteachessubject;
	}

	public Class getClazz() {
		return this.clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

}