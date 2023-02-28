package com.example.reactive.dao.course;

import com.example.reactive.dto.CourseDTO;
import com.example.reactive.entities.Course;
import com.example.reactive.entities.Student_Course;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
public class CourseRepositoryImpl implements CourseRepository{

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    private List<AggregationOperation> AggregationTemplate() {

        List<AggregationOperation> agg = new ArrayList<>(Arrays.asList(
                Aggregation.lookup("Student_Courses", "_id", "courseId", "enrolledStudentId"),
                Aggregation.lookup("Students", "enrolledStudentId.studentId", "_id", "enrolledStudents"),
                new ProjectionOperation().andExclude("enrolledStudentId")
        ));

        return agg;

    }

    private Mono<Boolean> isCourseExists(Course course) {
        return reactiveMongoTemplate.exists(new Query(
                        Criteria.where("name").is(course.getName()).and("fees").is(course.getFees()).and("educator").is(course.getEducator())),
                Course.class);
    }


    @Override
    public Flux<CourseDTO> fetchAllCourses() {
        List<AggregationOperation> aggl = AggregationTemplate();
        Aggregation agg = Aggregation.newAggregation(aggl);
        Flux<CourseDTO> aggrt = reactiveMongoTemplate.aggregate(agg,
                Course.class, CourseDTO.class);
        return aggrt;
    }

    @Override
    public Mono<CourseDTO> fetchCourse(String id) {
        List<AggregationOperation> aggl = new ArrayList<>();
        aggl.add(Aggregation.match(new Criteria("_id").is((new ObjectId(id)))));
        aggl.addAll(AggregationTemplate());
        Aggregation agg = Aggregation.newAggregation(aggl);
        Flux<CourseDTO> aggrt = reactiveMongoTemplate.aggregate(agg,
                Course.class, CourseDTO.class);
        return Mono.from(aggrt);
    }

    @Override
    public Mono<Course> storeCourse(Course course) {
        return isCourseExists(course).flatMap(isCourse -> {
                    if (isCourse) {
                        Mono<Course> crs = Mono.empty();
                        return crs;
                    } else {
                        return reactiveMongoTemplate.insert(course);
                    }
                });
    }


    @Override
    public Mono<Course> updateCourse(Course newCourse) {
        Query query = new Query(Criteria.where("_id").is(newCourse.getCourseId()));
        return reactiveMongoTemplate.findAndReplace(query, newCourse, FindAndReplaceOptions.options().returnNew());
    }

    @Override
    public void deleteCourse(String id) {
        Query query = new Query(Criteria.where("courseId").is(id));
        reactiveMongoTemplate.remove(query, Course.class).subscribe();
        reactiveMongoTemplate.remove(query, Student_Course.class).subscribe();
    }
}
