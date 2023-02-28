package com.example.reactive.dao.student;

import com.example.reactive.dto.StudentDTO;
import com.example.reactive.entities.Student;
import com.example.reactive.entities.Student_Course;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;


    private List<AggregationOperation> AggregationTemplate() {

        List<AggregationOperation> agg = new ArrayList<>(Arrays.asList(
                Aggregation.lookup("Student_Courses", "_id", "studentId", "enrolledCourseId"),
                Aggregation.lookup("Courses", "enrolledCourseId.courseId", "_id", "enrolledCourses"),
                new ProjectionOperation().andExclude("enrolledCourseId")));

        return agg;

    }

    private Mono<Boolean> isStudentExists(Student student) {
        return reactiveMongoTemplate.exists(new Query(
                        Criteria.where("name").is(student.getName()).and("college").is(student.getCollege())),
                Student.class);
    }


    @Override
    public Flux<StudentDTO> fetchAllStudents() {
        List<AggregationOperation> aggl = AggregationTemplate();
        Aggregation agg = Aggregation.newAggregation(aggl);
        Flux<StudentDTO> aggrt = reactiveMongoTemplate.aggregate(agg,
                Student.class, StudentDTO.class);
        return aggrt;
    }

    @Override
    public Mono<StudentDTO> fetchStudent(String id) {
        List<AggregationOperation> aggl = new ArrayList<>();
        aggl.add(Aggregation.match(new Criteria("_id").is((new ObjectId(id)))));
        aggl.addAll(AggregationTemplate());
        Aggregation agg = Aggregation.newAggregation(aggl);
        Flux<StudentDTO> aggrt = reactiveMongoTemplate.aggregate(agg,
                Student.class, StudentDTO.class);
        return Mono.from(aggrt);

    }

    @Override
    public Mono<Student> storeStudent(Student student) {
        return isStudentExists(student).flatMap(isStudent -> {
            if (isStudent) {
                Mono<Student> std = Mono.empty();
                return std;
            } else{
                return reactiveMongoTemplate.insert(student);
            }
        });
    }

    @Override
    public Mono<Student> updateStudent(Student newStudent) {
        Query query = new Query(Criteria.where("studentId").is(newStudent.getStudentId()));
        return reactiveMongoTemplate.findAndReplace(query, newStudent, FindAndReplaceOptions.options().returnNew());

    }

    @Override
    public void deleteStudent(String id) {
        Query query = new Query(Criteria.where("studentId").is(id));
        reactiveMongoTemplate.remove(query, Student.class).subscribe();
        reactiveMongoTemplate.remove(query, Student_Course.class).subscribe();
    }

    @Override
    public Mono<Boolean> EnrollStudentInCourse(String studentId, String courseId) {
        Student_Course studentCourse = new Student_Course(new ObjectId(studentId), new ObjectId(courseId));
        return reactiveMongoTemplate.exists(new Query(Criteria.where("studentId").is(new ObjectId(studentId)).and("courseId").is(new ObjectId(courseId))), Student_Course.class)
                .flatMap(isEnrolled -> {
                    if (isEnrolled)
                        return Mono.just(false);
                    else {
                        reactiveMongoTemplate.insert(studentCourse).subscribe();
                        return Mono.just(true);
                    }
                });
    }

    @Override
    public void UnenrollStudentFromCourse(String studentId, String courseId) {
        Query query = new Query(Criteria.where("studentId").is(new ObjectId(studentId))
                .and("courseId").is(new ObjectId(courseId)));
        reactiveMongoTemplate.remove(query, Student_Course.class).subscribe();
    }
}
