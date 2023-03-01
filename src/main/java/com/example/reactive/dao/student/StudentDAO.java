package com.example.reactive.dao.student;

import com.example.reactive.dto.StudentDTO;
import com.example.reactive.entities.Student;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class StudentDAO {

    @Autowired
    private StudentRepository studentRepository;

    public Flux<StudentDTO> getAllStudents() {
        return studentRepository.fetchAllStudents();
    }

    public Mono<StudentDTO> getStudent(String id) {
        return studentRepository.fetchStudent(id);
    }

    public Mono<Student> addStudent(Student student) {
        return studentRepository.storeStudent(student);
    }

    public Mono<Student> updateStudent(Student student) {
        return studentRepository.updateStudent(student);
    }

    public Mono<DeleteResult> deleteStudent(String id) {
        return studentRepository.deleteStudent(id);
    }

    public Mono<Boolean> EnrollIntoCourse(String studentId, String courseId) {
        return studentRepository.EnrollStudentInCourse(studentId, courseId).hasElement().flatMap(e ->{
            if(e)
                return Mono.just(true);
            else
                return Mono.just(false);
        });
    }

    public Mono<DeleteResult> UnenrollFromCourse(String studentId, String courseId) {
        return studentRepository.UnenrollStudentFromCourse(studentId, courseId);
    }

}
