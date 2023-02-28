package com.example.reactive.services;

import com.example.reactive.dao.student.StudentDAO;
import com.example.reactive.dto.StudentDTO;
import com.example.reactive.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    public Mono<StudentDTO> convertToDTO(Mono<Student> student) {

         return student.flatMap(s -> {
            StudentDTO studentDTO = new StudentDTO(
                    s.getStudentId(), s.getName(), s.getCollege(), new HashSet<>());
            return Mono.just(studentDTO);
        });

    }

    public Student convertToEntity(StudentDTO studentDTO) {
        Student student = new Student(
                studentDTO.getId(), studentDTO.getName(), studentDTO.getCollege());
        return student;
    }

    public Flux<StudentDTO> getAllStudents() {
        Flux<StudentDTO> students = studentDAO.getAllStudents();
        return students;
    }

    public Mono<StudentDTO> getStudent(String id) {
        Mono<StudentDTO> student = studentDAO.getStudent(id);
        return student;
    }

    public Mono<StudentDTO> addStudent(StudentDTO studentDTO) {
        return convertToDTO(studentDAO.addStudent(convertToEntity(studentDTO)));

    }

    public Mono<StudentDTO> updateStudent(StudentDTO studentDTO) {
        Student student = convertToEntity(studentDTO);
        Mono<StudentDTO> newStudentDTO = convertToDTO(studentDAO.updateStudent(student));
        return newStudentDTO.flatMap(s -> {
            s.setEnrolledCourses(studentDTO.getEnrolledCourses());
            return Mono.just(s);
        });

    }

    public void deleteStudent(String id) {
        studentDAO.deleteStudent(id);
    }

    public Mono<Boolean> EnrollStudentIntoCourse(String studentId, String courseId) {
        return studentDAO.EnrollIntoCourse(studentId, courseId);
    }

    public void UnenrolLStudentFromCourse(String studentId, String courseId) {
        studentDAO.UnenrollFromCourse(studentId, courseId);
    }

}