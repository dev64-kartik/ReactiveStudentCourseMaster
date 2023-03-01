package com.example.reactive.controllers;

import com.example.reactive.dto.CourseDTO;
import com.example.reactive.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping("")
    public Mono<ResponseEntity<Flux<CourseDTO>>> getCourses() {
        Flux<CourseDTO> courses = courseService.getAllCourses();
        return courses.hasElements().map(c -> {
            if (c)
                return ResponseEntity.of(Optional.of(courses));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<CourseDTO>>> getCourse(@PathVariable String id) {
        Mono<CourseDTO> course = courseService.getCourse(id);
        return course.hasElement().map(c -> {
            if (c)
                return ResponseEntity.of(Optional.of(course));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    @PostMapping("")
    public Mono<ResponseEntity<Mono<CourseDTO>>> addCourse(@RequestBody CourseDTO course) {
        Mono<CourseDTO> courseDTO = courseService.addCourse(course);
        return courseDTO.hasElement().map(c -> {
            if (c)
                return ResponseEntity.of(Optional.of(courseDTO));
            else
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
        });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Mono<CourseDTO>>> updateCourse(@RequestBody CourseDTO course, @PathVariable String id) {
        course.setId(id);
        Mono<CourseDTO> crs = courseService.updateCourse(course);
        return crs.hasElement().map(c -> {
            if (c)
                return ResponseEntity.of(Optional.of(crs));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    //Delete specific course

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteCourse(@PathVariable String id) {
        return courseService.deleteCourse(id).map(deleteResult -> {
            if (deleteResult.wasAcknowledged()) {
                return ResponseEntity.of(Optional.of("Course Deleted Successfully !"));
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
        });
    }


}
