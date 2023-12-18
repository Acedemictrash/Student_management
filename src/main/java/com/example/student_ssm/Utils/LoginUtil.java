package com.example.student_ssm.Utils;

import com.example.student_ssm.entity.User;
import com.example.student_ssm.entity.VO.UserVO;
import com.example.student_ssm.exception.CustomException;

public class LoginUtil {

    /**
     * 登录验证
     * @param user 用户输入的信息
     * @param dbUser 数据库中的信息
     * @return 用户信息
     */
    public static UserVO loginVerify(UserVO user, User dbUser) {
        if (dbUser == null) {
            // 没有用户
            throw new CustomException("账号或密码错误");
        }

        if (!user.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException("账号或密码错误");
        }

        // 登录成功
        UserVO userVO = new UserVO();
        userVO.setUsername(dbUser.getUsername());
        userVO.setPassword(dbUser.getPassword());
        userVO.setNumber(dbUser.getNumber());
        userVO.setAvatar(dbUser.getAvatar());
        userVO.setLeftWeight(dbUser.getLeftWeight());
        return userVO;
    }

}
