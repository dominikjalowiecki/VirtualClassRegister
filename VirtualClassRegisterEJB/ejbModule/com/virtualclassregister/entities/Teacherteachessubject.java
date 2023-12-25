package com.virtualclassregister.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;


/**
 * The persistent class for the teacherteachessubject database table.
 * 
 */
@Entity
@NamedQuery(name="Teacherteachessubject.findAll", query="SELECT t FROM Teacherteachessubject t")
public class Teacherteachessubject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idTeacherTeachesSubject;

	//bi-directional many-to-one association to Lesson
	@OneToMany(mappedBy="teacherteachessubject")
	private List<Lesson> lessons;

	//bi-directional many-to-one association to Subject
	@ManyToOne
	@JoinColumn(name="idSubject")
	private Subject subject;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="teacher_idUser")
	private User user;

	public Teacherteachessubject() {
	}

	public int getIdTeacherTeachesSubject() {
		return this.idTeacherTeachesSubject;
	}

	public void setIdTeacherTeachesSubject(int idTeacherTeachesSubject) {
		this.idTeacherTeachesSubject = idTeacherTeachesSubject;
	}

	public List<Lesson> getLessons() {
		return this.lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public Lesson addLesson(Lesson lesson) {
		getLessons().add(lesson);
		lesson.setTeacherteachessubject(this);

		return lesson;
	}

	public Lesson removeLesson(Lesson lesson) {
		getLessons().remove(lesson);
		lesson.setTeacherteachessubject(null);

		return lesson;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}