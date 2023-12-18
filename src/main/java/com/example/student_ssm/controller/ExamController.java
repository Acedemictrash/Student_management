package com.example.student_ssm.controller;

import com.example.student_ssm.common.Result;
import com.example.student_ssm.entity.Exam;
import com.example.student_ssm.service.ExamService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/select")
    public Result examSelect(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize,
                             String number, String cid, String name) {

        PageInfo<Exam> exam = examService.examSelect(pageNum, pageSize, number, cid, name);
        return Result.success(exam);
    }

    @PostMapping("/insert")
    public Result examInsert(@RequestBody Exam exam) throws ParseException {
        examService.insert(exam);
        return Result.success();
    }

    @PutMapping("/update")
    public Result examUpdate(@RequestBody Exam exam) throws ParseException {
        examService.update(exam);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result examDelete(@RequestParam String cid) {
        examService.delete(cid);
        return Result.success();
    }
}
