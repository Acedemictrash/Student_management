package com.example.student_ssm.service;

import com.example.student_ssm.entity.Teacher;
import com.example.student_ssm.entity.VO.UserVO;
import com.example.student_ssm.mapper.TeacherMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.student_ssm.Utils.LoginUtil.loginVerify;

@Service
public class TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    public UserVO teacherLogin(UserVO teacher) {
        Teacher dbTeacher = teacherMapper.teacherSelectByNumber(teacher.getNumber());

        return loginVerify(teacher, dbTeacher);
    }

    public PageInfo<Teacher> teacherSelect(Integer pageNum, Integer pageSize, Teacher teacher) {
        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> list;
        if (StringUtils.hasLength(teacher.getUsername()) || StringUtils.hasLength(teacher.getNumber())) {
            list = teacherMapper.teacherSelectBy(teacher);
        } else {
            list = teacherMapper.selectAll();
        }

        return PageInfo.of(list);
    }

    public void insert(Teacher teacher) {
        teacherMapper.insert(teacher);
    }

    public void update(Teacher teacher) {
        if (StringUtils.hasLength(teacher.getPassword())) {
            teacherMapper.updatePassword(teacher);
        } else {
            teacherMapper.update(teacher);
        }
    }

    public void delete(String number) {
        teacherMapper.delete(number);
    }

    public List<Teacher> selectByName(String name) {
        return teacherMapper.selectByName(name);
    }
}
