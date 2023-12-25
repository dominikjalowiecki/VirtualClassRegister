package com.virtualclassregister.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


/**
 * The persistent class for the behaviourpoints database table.
 * 
 */
@Entity
@Table(name="behaviourpoints")
@NamedQuery(name="Behaviourpoint.findAll", query="SELECT b FROM Behaviourpoint b")
public class Behaviourpoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idBehaviourPoints;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false, updatable = false)
	private Date created;

	@Lob
	private String note;

	private byte value;

	//bi-directional many-to-one association to Class
	@ManyToOne
	@JoinColumn(name="idClass")
	private Class clazz;

	//bi-directional many-to-one association to Semester
	@ManyToOne
	@JoinColumn(name="idSemester")
	private Semester semester;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="student_idUser")
	private User user;

	public Behaviourpoint() {
	}

	public int getIdBehaviourPoints() {
		return this.idBehaviourPoints;
	}

	public void setIdBehaviourPoints(int idBehaviourPoints) {
		this.idBehaviourPoints = idBehaviourPoints;
	}

	public Date getCreated() {
		return this.created;
	}
	
	public String getCreatedDatetime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(this.created);
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

	public byte getValue() {
		return this.value;
	}

	public void setValue(byte value) {
		this.value = value;
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

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}