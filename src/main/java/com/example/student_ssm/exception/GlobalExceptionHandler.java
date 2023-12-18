package com.example.student_ssm.exception;

import com.example.student_ssm.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages="com.example.student_ssm.controller")
@Slf4j
public class GlobalExceptionHandler {
    // TODO: 拦截器

    // 统一异常处理 @ExceptionHandler, 主要用于 Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody // 返回 json 串
    public Result error(HttpServletRequest request, Exception e){
        log.error("异常信息：", e);
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody // 返回 json 串
    public Result customError(HttpServletRequest request, CustomException e){
        return Result.error(e.getMsg());
    }
}
