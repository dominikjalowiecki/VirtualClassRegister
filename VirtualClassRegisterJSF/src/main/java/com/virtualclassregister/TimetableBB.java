package com.virtualclassregister;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import com.virtualclassregister.dao.LessonDAO;
import com.virtualclassregister.entities.Class;
import com.virtualclassregister.entities.Lesson;
import com.virtualclassregister.entities.Semester;
import com.virtualclassregister.entities.User;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class TimetableBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	@ManagedProperty("#{textMessage}")
	private ResourceBundle textMessage;
	
	@EJB
	LessonDAO lessonDAO;
	
	@Inject
	FacesContext ctx;
	
	@Getter @Setter private int searchIdSemester;
	@Getter @Setter private int searchIdClass;
	@Getter @Setter private int searchIdTeacher;
	
	@Getter private ScheduleModel eventModel;
	
	@PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
	}
	
	public void search() {
		if(searchIdClass == 0 && searchIdTeacher == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Class or teacher is required!", null));
			return;
		}
		
		if(searchIdClass != 0 && searchIdTeacher != 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Choose class or teacher!", null));
			return;
		}
		
		Semester semester = new Semester();
		semester.setIdSemester(searchIdSemester);
		List<Lesson> lessons = new ArrayList<>();
		
		if(searchIdClass != 0) {
			Class clazz = new Class();
			clazz.setIdClass(searchIdClass);
			
			Map<String, Object> searchParams = new HashMap<>();
			searchParams.put("semester", semester);
			searchParams.put("clazz", clazz);
			lessons = lessonDAO.getList(searchParams);
		} else if(searchIdTeacher != 0) {
			User teacher = new User();
			teacher.setIdUser(searchIdTeacher);
			
			lessons = lessonDAO.getListByTeacher(semester, teacher);	
		}
		
		eventModel = new DefaultScheduleModel();
		
		for(Lesson lesson : lessons) {			
			int dayOfWeek = 1;
			switch(lesson.getDay()) {
				case "Niedziela":
					dayOfWeek = 1;
					break;
				case "Poniedziałek":
					dayOfWeek = 2;
					break;
				case "Wtorek":
					dayOfWeek = 3;
					break;
				case "Środa":
					dayOfWeek = 4;
					break;
				case "Czwartek":
					dayOfWeek = 5;
					break;
				case "Piątek":
					dayOfWeek = 6;
					break;
				case "Sobota":
					dayOfWeek = 7;
					break;
			}
			
			Calendar c = Calendar.getInstance();
			Time start = lesson.getStart();
			c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			c.set(Calendar.HOUR_OF_DAY, start.getHours());
			c.set(Calendar.MINUTE, start.getMinutes());
			c.set(Calendar.SECOND, start.getSeconds());
			
			String subjectName = lesson.getTeacherteachessubject().getSubject().getName();
			String teacherName = lesson.getTeacherteachessubject().getUser().getName();
			String clazzName = lesson.getClazz().getName();
			
	        DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
        		.title(subjectName)
        		.description(teacherName + "; Class " + clazzName)
                .startDate(LocalDateTime.ofInstant(c.toInstant(), ZoneId.systemDefault()))
                .overlapAllowed(true)
                .editable(false)
                .resizable(false)
                .borderColor("orange")
                .build();
	        
	        Time end = lesson.getEnd();
	        c.set(Calendar.HOUR_OF_DAY, end.getHours());
			c.set(Calendar.MINUTE, end.getMinutes());
			c.set(Calendar.SECOND, end.getSeconds());
	        event.setEndDate(LocalDateTime.ofInstant(c.toInstant(), ZoneId.systemDefault()));
	        eventModel.addEvent(event);
		}
	}
	
}
