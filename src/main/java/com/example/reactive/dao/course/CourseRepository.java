package com.example.reactive.dao.course;


import com.example.reactive.dto.CourseDTO;
import com.example.reactive.entities.Course;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface CourseRepository {

    Flux<CourseDTO> fetchAllCourses();

    Mono<CourseDTO> fetchCourse(String id);

    Mono<Course> storeCourse(Course course);

    Mono<Course> updateCourse(Course newCourse);

    void deleteCourse(String id);
}
