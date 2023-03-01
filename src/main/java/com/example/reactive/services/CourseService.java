package com.example.reactive.services;

import com.example.reactive.dao.course.CourseDAO;
import com.example.reactive.dto.CourseDTO;
import com.example.reactive.entities.Course;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseDAO courseDAO;

    public Mono<CourseDTO> convertToDTO(Mono<Course> course) {

        return course.flatMap(c -> {
        CourseDTO courseDTO = new CourseDTO(
                c.getCourseId(), c.getName(), c.getFees(), c.getEducator(), new ArrayList<>());
        return Mono.just(courseDTO);
        });
    }

    public Course convertToEntity(CourseDTO courseDTO) {
        Course course = new Course(
                courseDTO.getId(), courseDTO.getName(), courseDTO.getFees(), courseDTO.getEducator());
        return course;
    }

    public Flux<CourseDTO> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public Mono<CourseDTO> getCourse(String id) {
        return courseDAO.getCourse(id);
    }

    public Mono<CourseDTO> addCourse(CourseDTO courseDTO) {

        return convertToDTO(courseDAO.addCourse(convertToEntity(courseDTO)));
    }

    public Mono<CourseDTO> updateCourse(CourseDTO courseDTO) {
        Course course = convertToEntity(courseDTO);
        Mono<CourseDTO> newCourseDTO = convertToDTO(courseDAO.updateCourse(course));
        return newCourseDTO.flatMap(c -> {
            c.setEnrolledStudents(courseDTO.getEnrolledStudents());
            return Mono.just(c);
        });
    }

    public Mono<DeleteResult> deleteCourse(String id) {
        return courseDAO.deleteCourse(id);
    }

}

