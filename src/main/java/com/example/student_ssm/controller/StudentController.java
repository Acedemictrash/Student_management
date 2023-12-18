package com.example.student_ssm.controller;

import com.example.student_ssm.common.Result;
import com.example.student_ssm.entity.Score;
import com.example.student_ssm.entity.Student;
import com.example.student_ssm.entity.Teacher;
import com.example.student_ssm.service.StudentService;
import com.example.student_ssm.service.TeacherService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService StudentService;

    @RequestMapping("/select")
    public Result StudentSelect(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "5") Integer pageSize,
                                Student student) {
        PageInfo<Student> pageInfo = StudentService.studentSelect(pageNum, pageSize, student);

        return Result.success(pageInfo);
    }

    @PostMapping("/insert")
    public Result StudentInsert(@RequestBody Student student) {
        StudentService.insert(student);
        return Result.success();
    }

    @PutMapping("/update")
    public Result StudentUpdate(@RequestBody Student student) {
        StudentService.update(student);
        return Result.success();
    }

    @DeleteMapping("/delete/{number}")
    public Result StudentDelete(@PathVariable String number) {
        StudentService.delete(number);
        return Result.success();
    }

    @GetMapping("/selectGrade")
    public Result SelectGrade(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "5") Integer pageSize,
                              @RequestParam String number) {
        PageInfo<Score> pageInfo = StudentService.selectGrade(pageNum, pageSize, number);

        return Result.success(pageInfo);
    }
}
