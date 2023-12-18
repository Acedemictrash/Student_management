package com.example.student_ssm.service;

import com.example.student_ssm.entity.VO.SelectVO;
import com.example.student_ssm.entity.course.SelectCourse;
import com.example.student_ssm.entity.course.*;
import com.example.student_ssm.entity.Teacher;
import com.example.student_ssm.entity.VO.CourseVO;
import com.example.student_ssm.mapper.CourseMapper;
import com.example.student_ssm.mapper.TeacherMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.student_ssm.Utils.CourseUtil.getCourseTime;
import static com.example.student_ssm.Utils.CourseUtil.getCourseVO;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    // total - 查询的总数
    // list - 数据列表
    public PageInfo<CourseVO> selectPage(Integer pageNum, Integer pageSize, CourseVO courseVO) { // pageNum - 当前页码，pageSize - 每页显示的条数
        List<CourseReturn> list;
        if (StringUtils.hasLength(courseVO.getName()) || StringUtils.hasLength(courseVO.getId()) || StringUtils.hasLength(courseVO.getUsername()) || StringUtils.hasLength(courseVO.getSid())) {
            if (StringUtils.hasLength(courseVO.getSid()) && courseVO.getSid().charAt(0) == '-') {
                courseVO.setSid(courseVO.getSid().substring(1));
                list = courseMapper.selectCourseNotBy(courseVO);
            } else {
                list = courseMapper.selectCourseBy(courseVO);
            }
        } else {
            list = courseMapper.selectCourse();
        }


        List<CourseVO> reList = new ArrayList<>();

        int n1 = 0, n2 = 0;
        while (n2 < list.size()) {
            if (list.get(n1).getId().equals(list.get(n2).getId())) {
                n2++;
                if (list.get(n1).getId().equals(list.get(n2 - 1).getId()) && n2 == list.size()) {
                    CourseVO dbCourseVO = getCourseVO(list.get(n1));
                    reList.add(dbCourseVO);
                }
            } else {
                CourseVO dbCourseVO = getCourseVO(list.get(n1));
                reList.add(dbCourseVO);
                n1 = n2;
            }
        }

        for (int i = 0; i < reList.size(); i++) {
            CourseVO dbCourseVO = reList.get(i);
            List<CourseTime> reTime = dbCourseVO.getTime();

            for (int j = 0; j < list.size(); j++) {
                CourseReturn courseReturn = list.get(j);
                CourseTime courseTime = new CourseTime(courseReturn.getMonday(), courseReturn.getTuesday(), courseReturn.getWednesday(), courseReturn.getThursday(), courseReturn.getFriday(), courseReturn.getSaturday(), courseReturn.getSunday(), courseReturn.getStartTime(), courseReturn.getEndTime(), courseReturn.getStartWeek(), courseReturn.getEndWeek());

                if (dbCourseVO.getId().equals(courseReturn.getId())) {
                    for (int k = 0; k < reTime.size(); k++) {
                        CourseTime time = reTime.get(k);
                        if (!time.equals(courseTime)) {
                            dbCourseVO.setTime(courseTime);
                        }
                    }
                }
            }
            dbCourseVO.setTimeTotal(dbCourseVO.getTime().size());
        }

        Page<CourseVO> page = new Page<>(pageNum, pageSize); // pageNum - 当前页码，pageSize - 每页显示的条数

        int startIndex = (pageNum - 1) * pageSize, endIndex = pageNum * pageSize;

        if (startIndex > reList.size()) {
            startIndex = reList.size();
        }
        if (endIndex > reList.size()) {
            endIndex = reList.size();
        }

        page.addAll(reList.subList(startIndex, endIndex));
        page.setTotal(reList.size());

        return PageInfo.of(page);
    }

    public PageInfo<SelectVO> selectCoursePage(Integer pageNum, Integer pageSize, String cid) {
        PageHelper.startPage(pageNum, pageSize);
        List<SelectVO> list = courseMapper.selectCoursePage(cid);
        return PageInfo.of(list);
    }

    public void add(CourseFront courseFront) {

        List<Teacher> teachers = teacherMapper.selectByName(courseFront.getUsername());
        CourseToDb course = new CourseToDb(courseFront.getId(), courseFront.getName(), courseFront.getCredit(), teachers.get(0).getNumber(), courseFront.getClassroom(), courseFront.getMaxNum(), courseFront.getCurrentNum(), courseFront.getIntroduction());

        courseMapper.insertCourse(course);

        for (int i = 0; i < courseFront.getTime().size(); i++) {
            CourseTimeFront courseTimeFront = courseFront.getTime().get(i);
            CourseTime courseTime = getCourseTime(courseFront, courseTimeFront, i);
            courseMapper.insertCourseTime(courseTime);
        }
    }

    public void update(CourseFront courseFront) {

        List<Teacher> teachers = teacherMapper.selectByName(courseFront.getUsername());
        CourseToDb course = new CourseToDb(courseFront.getId(), courseFront.getName(), courseFront.getCredit(), teachers.get(0).getNumber(), courseFront.getClassroom(), courseFront.getMaxNum(), courseFront.getCurrentNum(), courseFront.getIntroduction());

        courseMapper.updateCourse(course);

        courseMapper.deleteCourseTime(courseFront.getId());

        for (int i = 0; i < courseFront.getTime().size(); i++) {
            CourseTimeFront courseTimeFront = courseFront.getTime().get(i);
            CourseTime courseTime = getCourseTime(courseFront, courseTimeFront, i);
            courseMapper.insertCourseTime(courseTime);
        }
    }

    public void delete(String id) {
        courseMapper.deleteCourse(id);
        courseMapper.deleteCourseTime(id);
    }

    public void addSelect(SelectCourse select) {
        select.setTid(teacherMapper.selectByName(select.getTname()).get(0).getNumber());
        courseMapper.addSelect(select);
        courseMapper.addStudentSelect(select);
        courseMapper.addCurrentNum(select.getCid());
    }

    public int getSelect(SelectCourse select) {
        return courseMapper.getSelect(select).size();
    }

    public void updateSelect(SelectCourse select) {
        select.setTid(teacherMapper.selectByName(select.getTname()).get(0).getNumber());
        courseMapper.updateSelect(select);
        select.setWeight(select.getWeight() - select.getOriginalWeight());
        courseMapper.addStudentSelect(select);
    }

    public void deleteSelect(SelectCourse select) {
        courseMapper.deleteSelect(select);
        courseMapper.deleteCurrentNum(select.getCid());
        select.setWeight(-select.getOriginalWeight());
        courseMapper.addStudentSelect(select);
    }

    public void updateGrade(SelectCourse selectCourse) {
        courseMapper.updateGrade(selectCourse);
    }
}
