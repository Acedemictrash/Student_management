package com.example.student_ssm.entity;

import com.example.student_ssm.entity.course.Course;
import lombok.Data;

@Data
public class Score {
    private String id;
    private String name;
    private int credit;
    private String number;
    private String username;
    private String teacher;
    private double grade = -1;
    private double gpa = calc();

    public double calc() {
        if (grade >= 85) {
            return 4.0;
        } else if (grade >= 60) {
            return 1.5 + (grade - 60) / 10;
        } else {
            return 0;
        }
    }

}
