package com.example.reactive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Student_Courses")
@Data
@AllArgsConstructor
public class Student_Course {

    private ObjectId studentId;
    private ObjectId courseId;

}