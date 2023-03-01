package com.example.reactive.dao.student;

import com.example.reactive.dto.StudentDTO;
import com.example.reactive.entities.Student;
import com.example.reactive.entities.Student_Course;
import com.mongodb.client.result.DeleteResult;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentRepository {

    Flux<StudentDTO> fetchAllStudents();

    Mono<StudentDTO> fetchStudent(String id);

    Mono<Student> storeStudent(Student student);

    Mono<Student> updateStudent(Student student);

    Mono<DeleteResult> deleteStudent(String id);

    Mono<Student_Course> EnrollStudentInCourse(String studentId, String courseId);

    Mono<DeleteResult> UnenrollStudentFromCourse(String studentId, String courseId);
}
