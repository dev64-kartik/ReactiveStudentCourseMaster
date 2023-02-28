package com.example.reactive.dto;

import com.example.reactive.entities.Student;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Component
@JsonPropertyOrder({"courseId", "name", "college", "enrolledCourses"})
public class CourseDTO {

    @JsonProperty("courseId")
    private String id;
    private String name;
    private int fees;
    private String educator;
    @JsonIgnoreProperties("_class")
    private List<Student> enrolledStudents;

}
