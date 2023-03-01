package com.example.reactive.dao.course;

import com.example.reactive.dto.CourseDTO;
import com.example.reactive.entities.Course;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CourseDAO {

    @Autowired
    private CourseRepository courseRepository;

    public Flux<CourseDTO> getAllCourses() {
        return courseRepository.fetchAllCourses();
    }

    public Mono<CourseDTO> getCourse(String id) {
        return courseRepository.fetchCourse(id);
    }

    public Mono<Course> addCourse(Course course) {
        return courseRepository.storeCourse(course);
    }

    public Mono<Course> updateCourse(Course newCourse) {
        return courseRepository.updateCourse(newCourse);
    }

    public Mono<DeleteResult> deleteCourse(String id) {
        return courseRepository.deleteCourse(id);
    }

}

