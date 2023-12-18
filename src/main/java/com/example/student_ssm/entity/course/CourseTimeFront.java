package com.example.student_ssm.entity.course;

import lombok.Data;

@Data
public class CourseTimeFront {
    private String[] week;
    private int startWeek;
    private int endWeek;
    private String startTime;
    private String endTime;
}
