package com.in28minutes.soap.webservices.soapcoursemanagement.soap.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.in28minutes.soap.webservices.soapcoursemanagement.soap.bean.Course;

@Component
public class CourseDetailsService {
	
	public enum Status {
		SUCCESS, FAILURE;
	}
	
	private static List<Course> courses = new ArrayList<Course>();
	
	static {
		Course course1 = new Course(1,"Spring","10 steps");
		courses.add(course1);
		
		Course course2 = new Course(1,"Spring MVC","10 steps");
		courses.add(course2);
		
		Course course3 = new Course(1,"Spring Boot","10 steps");
		courses.add(course3);
		
		Course course4 = new Course(1,"Maven","10 steps");
		courses.add(course4);
	}
	
	public Course findById(int id) {
		for(Course course: courses) {
			if (course.getId() == id) {
				return course;
			}
		}
		return null;
	}
	
	public List<Course> findAll() {
		return courses;	
	}
	
	public Status deleteById(int id) {
		Iterator<Course> it = courses.iterator();
		while (it.hasNext()) {
			Course course = it.next();
			if (course.getId() == id) {
				it.remove();
				return Status.SUCCESS;
			}
		}
		return Status.FAILURE;
	}
	

}
