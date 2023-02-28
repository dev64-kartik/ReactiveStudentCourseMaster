package com.example.reactive.dto;

import com.example.reactive.entities.Course;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"studentId", "name", "college", "enrolledCourses"})
public class StudentDTO {

    @JsonProperty("studentId")
    private String id;
    private String name;
    private String college;
    private Set<Course> enrolledCourses;
}
