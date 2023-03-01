package com.example.reactive.controllers;


import com.example.reactive.dto.StudentDTO;
import com.example.reactive.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("")
    public Mono<ResponseEntity<Flux<StudentDTO>>> getStudents() {
        Flux<StudentDTO> students = studentService.getAllStudents();
        return students.hasElements().map(e -> {
            if (e)
                return ResponseEntity.of(Optional.of(students));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<StudentDTO>>> getStudent(@PathVariable String id) {
        Mono<StudentDTO> student = studentService.getStudent(id);
        return student.hasElement().map(e -> {
            if (e)
                return ResponseEntity.of(Optional.of(student));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });

    }

    @PostMapping("")
    public Mono<ResponseEntity<Mono<StudentDTO>>> addStudent(@RequestBody StudentDTO student) {
        Mono<StudentDTO> studentDTO = studentService.addStudent(student);
        return studentDTO.hasElement().map(s -> {
            if (s)
                return ResponseEntity.of(Optional.of(studentDTO));
            else
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
        });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Mono<StudentDTO>>> updateStudent(@RequestBody StudentDTO student, @PathVariable String id) {
        student.setId(id);
        Mono<StudentDTO> crs = studentService.updateStudent(student);
        return crs.hasElement().map(s -> {
            if (s)
                return ResponseEntity.of(Optional.of(crs));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    //Delete specific student

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteStudent(@PathVariable String id) {
        return studentService.deleteStudent(id).map(deleteResult -> {
            if (deleteResult.wasAcknowledged())
                return ResponseEntity.of(Optional.of("Course Deleted Successfully !"));
            else
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        });
    }

    //Enroll student in course

    @PostMapping("/{id}/courses/{courseId}")
    public Mono<ResponseEntity<String>> enrollInCourse(@PathVariable String id, @PathVariable String courseId) {
        return studentService.EnrollStudentIntoCourse(id, courseId).map(success -> {
            if (success) {
                return ResponseEntity.of(Optional.of("Successfully Enrolled !"));
            } else
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
        });
    }

    @DeleteMapping("/{id}/courses/{courseId}")
    public Mono<ResponseEntity<String>> unenrollFromCourse(@PathVariable String id, @PathVariable String courseId) {
        return studentService.UnenrolLStudentFromCourse(id, courseId).map(deleteResult -> {
            if (deleteResult.wasAcknowledged()) {
                return ResponseEntity.of(Optional.of("Successfully Unenrolled from Course !"));
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
        });
    }

}
