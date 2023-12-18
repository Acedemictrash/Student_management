package com.example.student_ssm.entity;

import lombok.Data;

import java.util.List;

@Data
public class Teacher extends User {
    private String number;// 工号/学号
    private String username;
    private String password;
    private String gender;
    private String phone;
    private String email;

    private String major;// 学院

    @Override
    public String getAvatar() {
        return null;
    }

    @Override
    public int getLeftWeight() {
        return 0;
    }
}
