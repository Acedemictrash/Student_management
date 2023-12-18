package com.example.student_ssm.controller;

import com.example.student_ssm.common.Result;
import com.example.student_ssm.entity.VO.UserVO;
import com.example.student_ssm.service.AdminService;
import com.example.student_ssm.service.StudentService;
import com.example.student_ssm.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class WebController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    // TODO: 登录拦截器

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result adminLogin(@RequestBody UserVO uservo) {

        if (!StringUtils.hasLength(uservo.getNumber()) || !StringUtils.hasLength(uservo.getPassword())) {
            return Result.error("用户名或密码为空");
        } else if (uservo.getIdentity() == null) {
            return Result.error("未选择身份");
        }

        UserVO dbUserVO;
        if (uservo.getIdentity().equals("管理员")) {
            dbUserVO = adminService.adminLogin(uservo);
            dbUserVO.setIdentity("管理员");
        } else if (uservo.getIdentity().equals("学生")) {
            dbUserVO = studentService.studentLogin(uservo);
            dbUserVO.setIdentity("学生");
        } else {
            dbUserVO = teacherService.teacherLogin(uservo);
            dbUserVO.setIdentity("教师");
        }

        return Result.success(dbUserVO);
    }
}
