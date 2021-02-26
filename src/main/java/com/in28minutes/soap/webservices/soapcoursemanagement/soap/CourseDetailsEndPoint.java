package com.in28minutes.soap.webservices.soapcoursemanagement.soap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.in28minutes.courses.CourseDetails;
import com.in28minutes.courses.DeleteCourseDetailsRequest;
import com.in28minutes.courses.DeleteCourseDetailsResponse;
import com.in28minutes.courses.GetAllCourseDetailsRequest;
import com.in28minutes.courses.GetAllCourseDetailsResponse;
import com.in28minutes.courses.GetCourseDetailsRequest;
import com.in28minutes.courses.GetCourseDetailsResponse;
import com.in28minutes.soap.webservices.soapcoursemanagement.soap.bean.Course;
import com.in28minutes.soap.webservices.soapcoursemanagement.soap.exception.CourseNotFoundException;
import com.in28minutes.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService;
import com.in28minutes.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService.Status;

import java.math.BigInteger;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Endpoint
public class CourseDetailsEndPoint {
	
	@Autowired
	CourseDetailsService service;
	
	private static final String NAMESPACE_URI = "http://in28minutes.com/courses";
	
	private static final Logger log = 
			LoggerFactory.getLogger(CourseDetailsEndPoint.class);
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart="GetCourseDetailsRequest")
	@ResponsePayload
	public GetCourseDetailsResponse getCourseDetails(@RequestPayload  GetCourseDetailsRequest request) {
		
		Course course = service.findById(request.getId().intValue());
		if (course == null) {
			throw new CourseNotFoundException("Invalid Course Id: "+request.getId().intValue());
		}
		return mapCourseDetails(course);
	}

		
	@PayloadRoot(namespace = NAMESPACE_URI, localPart="GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse getAllCourseDetails(@RequestPayload  GetAllCourseDetailsRequest request) {
		
		List<Course> courses = service.findAll();
		return mapAllCourseDetails(courses);
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart="DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse deleteCourseDetails(@RequestPayload  DeleteCourseDetailsRequest request) {
		DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
		Status status = service.deleteById(request.getId().intValue());
		response.setStatus(mapStatus(status));
		return response;
	}
	
	public com.in28minutes.courses.Status mapStatus(Status status) {
		if (status == Status.FAILURE) {
			return com.in28minutes.courses.Status.FAILURE;
		}
		return com.in28minutes.courses.Status.SUCCESS;
	}
	
	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
		CourseDetails courseDetails = mapCourse(course);
		response.setCourseDetails(courseDetails);
		return  response;
	}
	
	private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
		GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
		for(Course course: courses) {
			CourseDetails cd = mapCourse(course);
			response.getCourseDetails().add(cd);
		}
		return response;
	}
	
	private CourseDetails mapCourse(Course course) {
		CourseDetails courseDetails = new CourseDetails();
		courseDetails.setId(new BigInteger(String.valueOf(course.getId())));
		courseDetails.setName(course.getName());
		courseDetails.setDescription(course.getDescription());
		return courseDetails;
	}

}
