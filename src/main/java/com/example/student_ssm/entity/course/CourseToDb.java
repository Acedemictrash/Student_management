package com.example.student_ssm.entity.course;

import lombok.Data;

@Data
public class CourseToDb {
    private String id;// 选课号
    private String name;
    private double credit;// 学分
    private String tid;
    private String classroom;
    private int maxNum;// 课程最大容量
    private int currentNum;// 学生人数
    private String introduction;

    public CourseToDb(String id, String name, double credit, String tid, String classroom, int maxNum, int currentNum, String introduction) {
        this.id = id;
        this.name = name;
        this.credit = credit;
        this.tid = tid;
        this.classroom = classroom;
        this.maxNum = maxNum;
        this.currentNum = currentNum;
        this.introduction = introduction;
    }
}
