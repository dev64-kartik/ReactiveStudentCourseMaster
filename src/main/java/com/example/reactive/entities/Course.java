package com.example.reactive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Courses")
@Data
@AllArgsConstructor
public class Course {

    @Id
    private String courseId;
    private String name;
    private int fees;
    private String educator;

}
