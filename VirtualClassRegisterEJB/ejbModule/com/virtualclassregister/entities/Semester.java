package com.virtualclassregister.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


/**
 * The persistent class for the semester database table.
 * 
 */
@Entity
@NamedQuery(name="Semester.findAll", query="SELECT s FROM Semester s")
public class Semester implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idSemester;

	@Temporal(TemporalType.TIMESTAMP)
	private Date end;

	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	private Date start;

	//bi-directional many-to-one association to Behaviourpoint
	@OneToMany(mappedBy="semester")
	private List<Behaviourpoint> behaviourpoints;

	//bi-directional many-to-one association to Lesson
	@OneToMany(mappedBy="semester")
	private List<Lesson> lessons;
	
	public Semester() {
	}

	public int getIdSemester() {
		return this.idSemester;
	}

	public void setIdSemester(int idSemester) {
		this.idSemester = idSemester;
	}

	public Date getEnd() {
		return this.end;
	}
	
	public String getEndDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(this.end);
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return this.start;
	}
	
	public String getStartDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(this.start);
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public List<Behaviourpoint> getBehaviourpoints() {
		return this.behaviourpoints;
	}

	public void setBehaviourpoints(List<Behaviourpoint> behaviourpoints) {
		this.behaviourpoints = behaviourpoints;
	}

	public Behaviourpoint addBehaviourpoint(Behaviourpoint behaviourpoint) {
		getBehaviourpoints().add(behaviourpoint);
		behaviourpoint.setSemester(this);

		return behaviourpoint;
	}

	public Behaviourpoint removeBehaviourpoint(Behaviourpoint behaviourpoint) {
		getBehaviourpoints().remove(behaviourpoint);
		behaviourpoint.setSemester(null);

		return behaviourpoint;
	}

	public List<Lesson> getLessons() {
		return this.lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public Lesson addLesson(Lesson lesson) {
		getLessons().add(lesson);
		lesson.setSemester(this);

		return lesson;
	}

	public Lesson removeLesson(Lesson lesson) {
		getLessons().remove(lesson);
		lesson.setSemester(null);

		return lesson;
	}

}