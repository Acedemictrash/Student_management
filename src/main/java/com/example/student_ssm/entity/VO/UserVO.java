package com.example.student_ssm.entity.VO;

import lombok.Data;

@Data
public class UserVO {
    private int id;
    private String number;
    private int leftWeight;
    private String username;
    private String password;
    private String identity;
    private String avatar;
}
