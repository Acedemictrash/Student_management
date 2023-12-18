package com.example.student_ssm.entity;

import lombok.Data;

import java.util.List;

@Data
public class Student extends User {
    private String number;// 工号/学号
    private String username;
    private String password;
    private String gender;
    private String phone;
    private String email;
    private String grade;
    private double GPA;
    private int credit;
    private int leftWeight = 100;
    private String major;
    private String avatar;
    private List<Score> scores;
}
