package com.example.reactive.dao.student;

import com.example.reactive.dto.StudentDTO;
import com.example.reactive.entities.Student;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentRepository {

    Flux<StudentDTO> fetchAllStudents();

    Mono<StudentDTO> fetchStudent(String id);

    Mono<Student> storeStudent(Student student);

    Mono<Student> updateStudent(Student student);

    void deleteStudent(String id);

    Mono<Boolean> EnrollStudentInCourse(String studentId, String courseId);

    void UnenrollStudentFromCourse(String studentId, String courseId);
}
