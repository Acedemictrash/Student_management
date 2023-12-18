package com.example.student_ssm.service;

import com.example.student_ssm.entity.*;
import com.example.student_ssm.entity.VO.UserVO;
import com.example.student_ssm.exception.CustomException;
import com.example.student_ssm.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.student_ssm.Utils.LoginUtil.loginVerify;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public UserVO studentLogin(UserVO student) {
        Student dbStudent = studentMapper.studentSelectByNumber(student.getNumber());

        return loginVerify(student, dbStudent);
    }

    public PageInfo<Student> studentSelect(Integer pageNum, Integer pageSize, Student student) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> list;
        if (StringUtils.hasLength(student.getUsername()) || StringUtils.hasLength(student.getNumber())) {
            list = studentMapper.studentSelectBy(student);
        } else {
            list = studentMapper.selectAll();
        }

        return PageInfo.of(list);
    }

    public void insert(Student student) {
        String s = student.getGrade();
        s = s.substring(0, 4);
        student.setGrade(String.valueOf(Integer.parseInt(s) + 1));
        studentMapper.insert(student);
    }

    public void update(Student student) {
        if (StringUtils.hasLength(student.getAvatar())) {
            studentMapper.update(student);
        }
        if (StringUtils.hasLength(student.getPassword())) {
            studentMapper.updatePassword(student);
        } else {
            String s = student.getGrade();
            if (StringUtils.hasLength(s)) {
                if (s.length() > 4) {
                    s = s.substring(0, 4);
                    student.setGrade(String.valueOf(Integer.parseInt(s) + 1));
                } else {
                    student.setGrade(s);
                }
            }
            studentMapper.update(student);
        }
    }

    public void delete(String number) {
        studentMapper.delete(number);
    }

    public PageInfo<Score> selectGrade(Integer pageNum, Integer pageSize, String number) {
        PageHelper.startPage(pageNum, pageSize);
        List<Score> list = studentMapper.selectGrade(number);
        for (Score score : list) {
            score.setGpa(score.calc());
        }

        return PageInfo.of(list);
    }
}
