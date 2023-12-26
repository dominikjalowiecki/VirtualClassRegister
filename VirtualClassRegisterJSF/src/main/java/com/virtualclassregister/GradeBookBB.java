package com.virtualclassregister;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.simplesecurity.RemoteClient;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import com.virtualclassregister.dao.BehaviourpointDAO;
import com.virtualclassregister.dao.GradeDAO;
import com.virtualclassregister.entities.Behaviourpoint;
import com.virtualclassregister.entities.Grade;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;
import com.virtualclassregister.entities.User;
import com.virtualclassregister.utils.LessonWithGrades;

import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
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
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;
	
	@Inject
	@ManagedProperty("#{textMain}")
	private ResourceBundle textMain;
	
	@EJB
	GradeDAO gradeDAO;
	
	@EJB
	BehaviourpointDAO behaviourpointDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
		
	@Getter @Setter private Grade selectedGrade;
	@Getter @Setter private Behaviourpoint selectedBehaviourPoint;
	
	private List<Grade> grades;
	@Getter private List<LessonWithGrades> lessonsWithGrades;
	@Getter private List<Behaviourpoint> behaviourPoints;
	
	@Getter @Setter private int searchIdSemester;
	
	private User requestedUser;
	@Getter private User student;
	
	@Getter private HorizontalBarChartModel hbarModel;
	@Getter private LineChartModel lineModel;
	
	public void onLoad() throws IOException {
		student = (User) flash.get("student");
		
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		RemoteClient<User> remoteClient = RemoteClient.load(session);
		requestedUser = remoteClient.getDetails(); 
		
		if(student == null) {
			student = requestedUser;
			
			if(!"Student".equals(student.getRole())) {
				ctx.getExternalContext().getFlash().setKeepMessages(true);
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, textMessage.getString("invalid_operation"), null));
				if (!ctx.isPostback()) {
					ctx.getExternalContext().redirect("class.jsf");
					ctx.responseComplete();
				}
			}
		}
	}
	
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
		Semester semester = new Semester();
		semester.setIdSemester(searchIdSemester);
					
		grades = gradeDAO.getListByStudent(semester, student);
				
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("user", student);
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
		
		if("Administrator".equals(requestedUser.getRole())) {
			createHorizontalBarModel();
			createLineModel();
		}
	}
	
	private void createHorizontalBarModel() {
        hbarModel = new HorizontalBarChartModel();
        ChartData data = new ChartData();

        HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
        hbarDataSet.setLabel(textMain.getString("weighted_average"));
        
        List<Number> values = new ArrayList<>();
        List<String> bgColor = new ArrayList<>();
        List<String> borderColor = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        for(LessonWithGrades lwg : lessonsWithGrades) {
            values.add(lwg.getWeightedAverage());

            bgColor.add("rgba(255, 99, 132, 0.2)");

            borderColor.add("rgb(255, 99, 132)");

            labels.add(lwg.getSubjectName());
        }
        
        hbarDataSet.setData(values);
        hbarDataSet.setBackgroundColor(bgColor);
        hbarDataSet.setBorderColor(borderColor);
        hbarDataSet.setBorderWidth(1);
        data.addChartDataSet(hbarDataSet);
        data.setLabels(labels);
        hbarModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        cScales.addXAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText(textMain.getString("grade_weighted_average"));
        options.setTitle(title);

        hbarModel.setOptions(options);
	}
	
	private void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int behaviourPointsBalance = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String createdDate = null;
        String lastCreatedDate = null;
        for(Behaviourpoint behaviourPoint : behaviourPoints) {        	
        	createdDate = sdf.format(behaviourPoint.getCreated());
        	
        	if(lastCreatedDate != null && !createdDate.equals(lastCreatedDate)) {
        		values.add(behaviourPointsBalance);
            	labels.add(lastCreatedDate);
        	}
        	
        	behaviourPointsBalance += behaviourPoint.getValue();
        	
        	lastCreatedDate = createdDate;
        }
        
        if(lastCreatedDate != null) {
        	values.add(behaviourPointsBalance);
        	labels.add(lastCreatedDate);
        }
        
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel(textMain.getString("behaviour_points"));
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        options.setMaintainAspectRatio(false);
        Title title = new Title();
        title.setDisplay(true);
        title.setText(textMain.getString("behaviour_points_balance"));
        options.setTitle(title);

        Title subtitle = new Title();
        subtitle.setDisplay(true);
        options.setSubtitle(subtitle);

        lineModel.setOptions(options);
        lineModel.setData(data);
    }
	
	public String editClass() {
		flash.put("clazz", student.getClazz());
		
		return "editClass?faces-redirect=true";
	}
	
}
