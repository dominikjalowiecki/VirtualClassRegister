package com.virtualclassregister;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.simplesecurity.RemoteClient;

import com.virtualclassregister.dao.BehaviourpointDAO;
import com.virtualclassregister.dao.GradeDAO;
import com.virtualclassregister.entities.Behaviourpoint;
import com.virtualclassregister.entities.Grade;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;
import com.virtualclassregister.entities.User;
import com.virtualclassregister.utils.LessonWithGrades;

import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class GradeBookBB implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@EJB
	GradeDAO gradeDAO;
	
	@EJB
	BehaviourpointDAO behaviourpointDAO;
	
	@Inject
	FacesContext ctx;
		
	@Getter @Setter private Grade selectedGrade;
	@Getter @Setter private Behaviourpoint selectedBehaviourPoint;
	
	private List<Grade> grades;
	@Getter private List<LessonWithGrades> lessonsWithGrades;
	@Getter private List<Behaviourpoint> behaviourPoints;
	
	@Getter @Setter private int searchIdSemester;
	
	public BigDecimal getGradesWeightedAverage() {
		BigDecimal weightedAverage = new BigDecimal("0");
		BigDecimal weightSummary = new BigDecimal("0");
		
		for(Grade grade : grades) {
			weightedAverage = weightedAverage.add(grade.getValue().multiply(grade.getGradetype().getWeightage()));
			weightSummary = weightSummary.add(grade.getGradetype().getWeightage());
		}
		
		if(!BigDecimal.ZERO.equals(weightSummary)) {
			weightedAverage = weightedAverage.divide(weightSummary, 2, RoundingMode.HALF_UP);
		}
		
		return weightedAverage;
	}
	
	public int getBehaviourPointsSummary() {
		int summary = 0;
		
		for(Behaviourpoint behaviourpoint : behaviourPoints) {
			summary += behaviourpoint.getValue();
		}
		
		return summary;
	}
	
	public void search() {
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		RemoteClient<User> remoteClient = RemoteClient.load(session);
		User requestedUser = remoteClient.getDetails();
		
		Semester semester = new Semester();
		semester.setIdSemester(searchIdSemester);
					
		grades = gradeDAO.getListByStudent(semester, requestedUser);
				
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("user", requestedUser);
		searchParams.put("semester", semester);
		behaviourPoints = behaviourpointDAO.getList(searchParams);
		
		lessonsWithGrades = new ArrayList<>();
		LessonWithGrades lessonWithGrades = null;
		Lesson gradeLesson = null;
		int lastIdLesson = -1;
		for(Grade grade : grades) {
			gradeLesson = grade.getLesson();
			if(lastIdLesson != gradeLesson.getIdLesson()) {
				if(lastIdLesson != -1) {
					lessonsWithGrades.add(lessonWithGrades);
				}
				lessonWithGrades = new LessonWithGrades(
						gradeLesson.getTeacherteachessubject().getSubject().getName(),
						gradeLesson.getTeacherteachessubject().getUser().getName()
					);
			}
			lessonWithGrades.getGrades().add(grade);
			lastIdLesson = gradeLesson.getIdLesson();
		}
		if(lessonWithGrades != null) {
			lessonsWithGrades.add(lessonWithGrades);
		}
		
		for(LessonWithGrades lwg : lessonsWithGrades) {
			BigDecimal weightedAverage = new BigDecimal("0");
			BigDecimal weightSummary = new BigDecimal("0");
			
			for(Grade grade : lwg.getGrades()) {
				weightedAverage = weightedAverage.add(grade.getValue().multiply(grade.getGradetype().getWeightage()));
				weightSummary = weightSummary.add(grade.getGradetype().getWeightage());
			}
			
			if(!BigDecimal.ZERO.equals(weightSummary)) {
				weightedAverage = weightedAverage.divide(weightSummary, 2, RoundingMode.HALF_UP);
			}
			
			lwg.setWeightedAverage(weightedAverage);
		}
	}
	
}
