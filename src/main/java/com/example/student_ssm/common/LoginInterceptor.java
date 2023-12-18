package com.example.student_ssm.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 此方法返回一个 boolean 值，如果返回 true，表示放行，如果返回 false，表示验证失败，拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 用户登录业务判断
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // 登录成功，放行
            return true;
        }

        // 登录失败，拦截 - 可以跳转到登录页面，也可以返回 401/403 没有权限的状态码
        response.sendRedirect("localhost:517/login");
        // response.setStatus(401);

        return false;
    }
}

