package com.example.student_ssm.Utils;

import com.example.student_ssm.entity.course.CourseFront;
import com.example.student_ssm.entity.course.CourseReturn;
import com.example.student_ssm.entity.course.CourseTime;
import com.example.student_ssm.entity.VO.CourseVO;
import com.example.student_ssm.entity.course.CourseTimeFront;

public class CourseUtil {

    public static CourseVO getCourseVO(CourseReturn courseReturn) {
        CourseVO courseVO = new CourseVO();
        courseVO.setId(courseReturn.getId());
        courseVO.setName(courseReturn.getName());
        courseVO.setCredit(courseReturn.getCredit());
        courseVO.setUsername(courseReturn.getUsername());
        courseVO.setTime(new CourseTime(courseReturn.getMonday(), courseReturn.getTuesday(), courseReturn.getWednesday(), courseReturn.getThursday(), courseReturn.getFriday(), courseReturn.getSaturday(), courseReturn.getSunday(), courseReturn.getStartTime(), courseReturn.getEndTime(), courseReturn.getStartWeek(), courseReturn.getEndWeek()));
        courseVO.setClassroom(courseReturn.getClassroom());
        courseVO.setMaxNum(courseReturn.getMaxNum());
        courseVO.setCurrentNum(courseReturn.getCurrentNum());
        courseVO.setIntroduction(courseReturn.getIntroduction());
        courseVO.setWeight(courseReturn.getWeight());
        return courseVO;
    }

    public static CourseTime getCourseTime(CourseFront courseFront, CourseTimeFront courseTimeFront, int i) {
        CourseTime courseTime = new CourseTime();
        for (int j = 0; j < courseTimeFront.getWeek().length; j++) {
            switch (courseTimeFront.getWeek()[j]) {
                case "星期一" -> courseTime.setMonday(1);
                case "星期二" -> courseTime.setTuesday(1);
                case "星期三" -> courseTime.setWednesday(1);
                case "星期四" -> courseTime.setThursday(1);
                case "星期五" -> courseTime.setFriday(1);
                case "星期六" -> courseTime.setSaturday(1);
                case "星期日" -> courseTime.setSunday(1);
            }
        }
        courseTime.setCid(courseFront.getId());
        courseTime.setStartWeek(courseTimeFront.getStartWeek());
        courseTime.setEndWeek(courseTimeFront.getEndWeek());
        courseTime.setStartTime(courseTimeFront.getStartTime());
        courseTime.setEndTime(courseTimeFront.getEndTime());
        return courseTime;
    }
}
