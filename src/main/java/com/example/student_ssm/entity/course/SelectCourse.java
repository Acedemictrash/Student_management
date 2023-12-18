package com.example.student_ssm.entity.course;

import lombok.Data;

@Data
public class SelectCourse {

    private String cid;
    private String tname;
    private String sname;
    private String sid;
    private String tid;
    private double grade;
    private int weight;
    private int originalWeight;

}
