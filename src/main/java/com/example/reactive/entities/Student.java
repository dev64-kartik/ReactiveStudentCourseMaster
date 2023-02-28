package com.example.reactive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Students")
@Data
@AllArgsConstructor
public class Student {

    @Id
    private String studentId;
    private String name;
    private String college;

}
