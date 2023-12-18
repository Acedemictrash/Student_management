package com.example.student_ssm.entity.VO;

import com.example.student_ssm.entity.course.CourseTime;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CourseVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;// 选课号
    private String name;
    private double credit;// 学分
    private String username;// 老师姓名
    private String sid;
    private List<CourseTime> time = new ArrayList<>();
    private String classroom;
    private int maxNum;// 课程最大容量
    private int currentNum;// 学生人数
    private String introduction;
    private int timeTotal = 0;
    private int weight = -1;

    public void setTime(CourseTime courseTime) {
        time.add(courseTime);
    }
}
