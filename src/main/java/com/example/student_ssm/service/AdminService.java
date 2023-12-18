package com.example.student_ssm.service;

import com.example.student_ssm.entity.Admin;
import com.example.student_ssm.entity.Student;
import com.example.student_ssm.entity.VO.UserVO;
import com.example.student_ssm.exception.CustomException;
import com.example.student_ssm.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.student_ssm.Utils.LoginUtil.loginVerify;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 登录逻辑
     */
    public UserVO adminLogin(UserVO admin) {
        Admin dbAdmin = adminMapper.adminSelectByNumber(admin.getNumber());

        return loginVerify(admin, dbAdmin);
    }
}
