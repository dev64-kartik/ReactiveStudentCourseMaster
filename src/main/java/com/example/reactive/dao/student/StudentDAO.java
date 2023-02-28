package com.example.reactive.dao.student;

import com.example.reactive.dto.StudentDTO;
import com.example.reactive.entities.Student;
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

    public void deleteStudent(String id) {
        studentRepository.deleteStudent(id);
    }

    public Mono<Boolean> EnrollIntoCourse(String studentId, String courseId) {
        return studentRepository.EnrollStudentInCourse(studentId, courseId);
    }

    public void UnenrollFromCourse(String studentId, String courseId) {
        studentRepository.UnenrollStudentFromCourse(studentId, courseId);
    }

}
