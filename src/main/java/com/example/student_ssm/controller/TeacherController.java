package com.example.student_ssm.controller;

import com.example.student_ssm.common.Result;
import com.example.student_ssm.entity.Teacher;
import com.example.student_ssm.service.TeacherService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @RequestMapping("/select")
    public Result teacherSelect(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "5") Integer pageSize,
                                Teacher teacher) {
        System.out.println(teacher);
        PageInfo<Teacher> pageInfo = teacherService.teacherSelect(pageNum, pageSize, teacher);

        return Result.success(pageInfo);
    }

    @RequestMapping("/selectbyname")
    public Result teacherSelect(String username) {
        List<Teacher> teachers = teacherService.selectByName(username);

        return Result.success(teachers);
    }

    @PostMapping("/insert")
    public Result teacherInsert(@RequestBody Teacher teacher) {
        teacherService.insert(teacher);

        return Result.success();
    }

    @PutMapping("/update")
    public Result teacherUpdate(@RequestBody Teacher teacher) {
        teacherService.update(teacher);

        return Result.success();
    }

    @DeleteMapping("/delete/{number}")
    public Result teacherDelete(@PathVariable String number) {
        System.out.println(number);
        teacherService.delete(number);

        return Result.success();
    }

}
