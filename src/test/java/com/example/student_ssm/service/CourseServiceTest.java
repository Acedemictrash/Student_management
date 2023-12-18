package com.example.student_ssm.service;

import com.example.student_ssm.entity.Teacher;
import com.example.student_ssm.entity.course.CourseFront;
import com.example.student_ssm.entity.course.CourseTimeFront;
import com.example.student_ssm.entity.course.CourseToDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Test
    void add() {
        CourseFront courseFront = new CourseFront();
        List<CourseTimeFront> list = new ArrayList<>();
        CourseTimeFront courseTimeFront = new CourseTimeFront();
        String[] week = {"星期二", "星期四"};
        courseTimeFront.setWeek(week);
        courseFront.setUsername("傅翀");
        list.add(courseTimeFront);
        courseFront.setTime(list);
        courseService.add(courseFront);
    }

//    @Test
//    void selectPage() {
//        courseService.selectPage(1, 10, "awa");
//    }
}