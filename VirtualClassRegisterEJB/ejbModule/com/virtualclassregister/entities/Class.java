package com.virtualclassregister.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the class database table.
 * 
 */
@Entity
@NamedQuery(name="Class.findAll", query="SELECT c FROM Class c")
public class Class implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idClass;

	private String name;

	//bi-directional many-to-one association to Behaviourpoint
	@OneToMany(mappedBy="clazz")
	private List<Behaviourpoint> behaviourpoints;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="tutor_idUser")
	private User user;

	//bi-directional many-to-one association to Lesson
	@OneToMany(mappedBy="clazz", fetch=FetchType.EAGER)
	private List<Lesson> lessons;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="clazz")
	private List<User> users;
	
	public Class(Class clazz) {
		this.idClass = clazz.idClass;
		this.name = clazz.name;
		this.behaviourpoints = clazz.behaviourpoints;
		this.user = clazz.user;
		this.lessons = clazz.lessons;
		this.users = clazz.users;
	}

	public Class() {
	}

	public int getIdClass() {
		return this.idClass;
	}

	public void setIdClass(int idClass) {
		this.idClass = idClass;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Behaviourpoint> getBehaviourpoints() {
		return this.behaviourpoints;
	}

	public void setBehaviourpoints(List<Behaviourpoint> behaviourpoints) {
		this.behaviourpoints = behaviourpoints;
	}

	public Behaviourpoint addBehaviourpoint(Behaviourpoint behaviourpoint) {
		getBehaviourpoints().add(behaviourpoint);
		behaviourpoint.setClazz(this);

		return behaviourpoint;
	}

	public Behaviourpoint removeBehaviourpoint(Behaviourpoint behaviourpoint) {
		getBehaviourpoints().remove(behaviourpoint);
		behaviourpoint.setClazz(null);

		return behaviourpoint;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Lesson> getLessons() {
		return this.lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public Lesson addLesson(Lesson lesson) {
		getLessons().add(lesson);
		lesson.setClazz(this);

		return lesson;
	}

	public Lesson removeLesson(Lesson lesson) {
		getLessons().remove(lesson);
		lesson.setClazz(null);

		return lesson;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setClazz(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setClazz(null);

		return user;
	}

}