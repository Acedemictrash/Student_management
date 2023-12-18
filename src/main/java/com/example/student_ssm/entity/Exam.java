package com.example.student_ssm.entity;

import com.example.student_ssm.entity.course.Course;
import lombok.Data;

import java.util.Date;

@Data
public class Exam {
    private String cid;
    private String name;
    private String date;
    private String introduction;
    private String type;// 期中/期末
    private String other;
}
