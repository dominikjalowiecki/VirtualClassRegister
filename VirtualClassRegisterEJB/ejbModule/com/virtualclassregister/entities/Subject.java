package com.virtualclassregister.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;


/**
 * The persistent class for the subject database table.
 * 
 */
@Entity
@NamedQuery(name="Subject.findAll", query="SELECT s FROM Subject s")
public class Subject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idSubject;

	private String name;

	//bi-directional many-to-one association to Teacherteachessubject
	@OneToMany(mappedBy="subject", fetch=FetchType.EAGER)
	private List<Teacherteachessubject> teacherteachessubjects;

	public Subject(Subject subject) {
		this.idSubject = subject.idSubject;
		this.name = subject.name;
		this.teacherteachessubjects = subject.teacherteachessubjects;
	}
	
	public Subject() {
	}

	public int getIdSubject() {
		return this.idSubject;
	}

	public void setIdSubject(int idSubject) {
		this.idSubject = idSubject;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Teacherteachessubject> getTeacherteachessubjects() {
		return this.teacherteachessubjects;
	}

	public void setTeacherteachessubjects(List<Teacherteachessubject> teacherteachessubjects) {
		this.teacherteachessubjects = teacherteachessubjects;
	}

	public Teacherteachessubject addTeacherteachessubject(Teacherteachessubject teacherteachessubject) {
		getTeacherteachessubjects().add(teacherteachessubject);
		teacherteachessubject.setSubject(this);

		return teacherteachessubject;
	}

	public Teacherteachessubject removeTeacherteachessubject(Teacherteachessubject teacherteachessubject) {
		getTeacherteachessubjects().remove(teacherteachessubject);
		teacherteachessubject.setSubject(null);

		return teacherteachessubject;
	}

}