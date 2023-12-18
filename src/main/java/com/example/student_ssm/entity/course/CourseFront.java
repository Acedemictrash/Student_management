package com.example.student_ssm.entity.course;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseFront {

    private String id;// 选课号
    private String name;
    private double credit;// 学分
    private String username;// 老师姓名
    private List<CourseTimeFront> time = new ArrayList<>();
    private String classroom;
    private int maxNum;// 课程最大容量
    private int currentNum;// 学生人数
    private String introduction;

}
