package com.example.student_ssm.entity;

import lombok.Data;

@Data
public class Admin extends User {
    private int id;// 主键，id
    private String number;// 工号/学号
    private String username;
    private String password;


    @Override
    public String getAvatar() {
        return null;
    }

    @Override
    public int getLeftWeight() {
        return 0;
    }
}

// 1.创建实体类 - 创建数据库
// 2.创建 VO 类
// 3.更改登录方式