package com.virtualclassregister.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * The persistent class for the lesson database table.
 * 
 */
@Entity
@NamedQuery(name="Lesson.findAll", query="SELECT l FROM Lesson l")
public class Lesson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idLesson;

	private String day;

	private Time end;

	private Time start;

	//bi-directional many-to-one association to Announcement
	@OneToMany(mappedBy="lesson")
	private List<Announcement> announcements;

	//bi-directional many-to-one association to Grade
	@OneToMany(mappedBy="lesson")
	private List<Grade> grades;

	//bi-directional many-to-one association to Class
	@ManyToOne
	@JoinColumn(name="idClass")
	private Class clazz;

	//bi-directional many-to-one association to Semester
	@ManyToOne
	@JoinColumn(name="idSemester")
	private Semester semester;

	//bi-directional many-to-one association to Teacherteachessubject
	@ManyToOne
	@JoinColumn(name="idTeacherTeachesSubject")
	private Teacherteachessubject teacherteachessubject;

	public Lesson(Lesson lesson) {
		this.idLesson = lesson.idLesson;
		this.day = lesson.day;
		this.start = lesson.start;
		this.end = lesson.end;
		this.announcements = lesson.announcements;
		this.grades = lesson.grades;
		this.clazz = lesson.clazz;
		this.semester = lesson.semester;
		this.teacherteachessubject = lesson.teacherteachessubject;
	}
	
	public Lesson() {
	}

	public int getIdLesson() {
		return this.idLesson;
	}

	public void setIdLesson(int idLesson) {
		this.idLesson = idLesson;
	}

	public String getDay() {
		return this.day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Time getEnd() {
		return this.end;
	}
	
	public String getEndTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		return sdf.format(this.start);
	}

	public void setEnd(Time end) {
		this.end = end;
	}

	public Time getStart() {
		return this.start;
	}
	
	public String getStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		return sdf.format(this.start);
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public List<Announcement> getAnnouncements() {
		return this.announcements;
	}

	public void setAnnouncements(List<Announcement> announcements) {
		this.announcements = announcements;
	}

	public Announcement addAnnouncement(Announcement announcement) {
		getAnnouncements().add(announcement);
		announcement.setLesson(this);

		return announcement;
	}

	public Announcement removeAnnouncement(Announcement announcement) {
		getAnnouncements().remove(announcement);
		announcement.setLesson(null);

		return announcement;
	}

	public List<Grade> getGrades() {
		return this.grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public Grade addGrade(Grade grade) {
		getGrades().add(grade);
		grade.setLesson(this);

		return grade;
	}

	public Grade removeGrade(Grade grade) {
		getGrades().remove(grade);
		grade.setLesson(null);

		return grade;
	}

	public Class getClazz() {
		return this.clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Semester getSemester() {
		return this.semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public Teacherteachessubject getTeacherteachessubject() {
		return this.teacherteachessubject;
	}

	public void setTeacherteachessubject(Teacherteachessubject teacherteachessubject) {
		this.teacherteachessubject = teacherteachessubject;
	}

}