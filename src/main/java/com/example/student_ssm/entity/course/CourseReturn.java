package com.example.student_ssm.entity.course;

import lombok.Data;

@Data
public class CourseReturn {

    private String id;// 选课号
    private String name;
    private double credit;// 学分
    private String username;// 老师姓名
    private int startWeek;
    private int endWeek;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
    private int weight = -1;
    private String startTime;
    private String endTime;
    private String classroom;
    private int maxNum;// 课程最大容量
    private int currentNum;// 学生人数
    private String introduction;

}
