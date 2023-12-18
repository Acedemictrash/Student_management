package com.example.student_ssm.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.student_ssm.common.Result;
import com.example.student_ssm.entity.VO.SelectVO;
import com.example.student_ssm.entity.course.SelectCourse;
import com.example.student_ssm.entity.course.CourseFront;
import com.example.student_ssm.entity.VO.CourseVO;
import com.example.student_ssm.service.CourseService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             CourseVO courseVO) {
        PageInfo<CourseVO> courseVOPageInfo = courseService.selectPage(pageNum, pageSize, courseVO);

        return Result.success(courseVOPageInfo);
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody Map<String, Object> map) {

        JSONObject jsonObject = new JSONObject(map);
        CourseFront courseFront = jsonObject.toJavaObject(CourseFront.class);
        courseService.add(courseFront);

        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Map<String, Object> map) {

        JSONObject jsonObject = new JSONObject(map);
        CourseFront courseFront = jsonObject.toJavaObject(CourseFront.class);
        courseService.update(courseFront);

        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        courseService.delete(id);

        return Result.success();
    }

    @PostMapping("/addSelect")
    public Result addCourse(@RequestBody SelectCourse select) {
        System.out.println(select);
        courseService.addSelect(select);

        return Result.success();
    }

    @GetMapping("/getSelect")
    public Result getSelect(@RequestParam String sid, @RequestParam String cid) {
        SelectCourse select = new SelectCourse();
        System.out.println(sid);
        System.out.println(cid);
        select.setSid(sid);
        select.setCid(cid);
        int count = courseService.getSelect(select);
        return Result.success(count);
    }

    @PutMapping("/updateSelect")
    public Result updateSelect(@RequestBody SelectCourse select) {
        courseService.updateSelect(select);
        return Result.success();
    }

    @DeleteMapping("/deleteSelect")
    public Result deleteSelect(@RequestParam String sid, @RequestParam String cid, @RequestParam int originalWeight) {
        SelectCourse select = new SelectCourse();
        select.setSid(sid);
        select.setCid(cid);
        select.setOriginalWeight(originalWeight);
        courseService.deleteSelect(select);
        return Result.success();
    }

    @GetMapping("/getSelectByCid")
    public Result getSelectByCid(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "15") Integer pageSize,
                                 @RequestParam String cid) {
        PageInfo<SelectVO> selectCoursePageInfo = courseService.selectCoursePage(pageNum, pageSize, cid);
        return Result.success(selectCoursePageInfo);
    }

    @PutMapping("/updateGrade")
    public Result updateGrade(@RequestBody SelectCourse selectCourse) {
        courseService.updateGrade(selectCourse);
        return Result.success();
    }
}




