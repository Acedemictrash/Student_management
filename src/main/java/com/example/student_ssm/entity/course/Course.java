package com.example.student_ssm.entity.course;

import com.example.student_ssm.entity.Student;
import com.example.student_ssm.entity.Teacher;
import lombok.Data;

import java.util.List;

@Data
public class Course {

    private String id;// 选课号
    private String name;
    private int credit;// 学分
    private Teacher teacher;
    private List<CourseTime> time;
    private String classroom;
    private int maxNum;// 课程最大容量
    private int currentNum;// 学生人数
    private String introduction;
    private List<Student> students;

}
