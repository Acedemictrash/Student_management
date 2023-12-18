package com.example.student_ssm.service;

import com.example.student_ssm.Utils.DateUtil;
import com.example.student_ssm.entity.Exam;
import com.example.student_ssm.mapper.ExamMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamMapper examMapper;

    public PageInfo<Exam> examSelect(Integer pageNum, Integer pageSize, String number, String cid, String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<Exam> list;
        if (StringUtils.hasLength(number)) {
            list = examMapper.examSelect(number);
        } else {
            list = examMapper.examSelectAll(cid, name);
        }

        return PageInfo.of(list);
    }

    public void insert(Exam exam) throws ParseException {
        String time = exam.getIntroduction().split(",")[0];
        String[] date = time.split(" ")[0].split("-");
        int w = DateUtil.getWeek(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        int weeks;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Integer.parseInt(date[1]) > 1 && Integer.parseInt(date[1]) < 8) {
            int year = Integer.parseInt(date[0]);
            int gap = DateUtil.getWeek(year, 2, 25);
            int d;
            if (gap > 3) {
                d = 25 + gap;
            } else {
                d = 25 - gap;
            }
            Date std = df.parse(year + "-02-" + d + " 00:00:00");
            Date now = df.parse(time.substring(0, 10) + " 00:00:00");
            long diff = now.getTime() - std.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            weeks = (int) days / 7 + 1;
        } else {
            int year = Integer.parseInt(date[0]);
            if (Integer.parseInt(date[1]) < 2) {
                year -= 1;
            }
            int gap = DateUtil.getWeek(year, 8, 27);
            int d;
            if (gap > 3) {
                d = 27 + gap;
            } else {
                d = 27 - gap;
            }
            Date std = df.parse(year + "-08-" + d + " 00:00:00");
            Date now = df.parse(time.substring(0, 10) + " 00:00:00");
            long diff = now.getTime() - std.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            weeks = (int) days / 7 + 1;
        }
        String[] week = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        exam.setDate(time.substring(0, 10) + "（第" + weeks + "周 " + " " + week[w - 1] + "）");
        System.out.println(exam.getDate());

        examMapper.insert(exam);
    }

    public void update(Exam exam) throws ParseException {
        String time = exam.getIntroduction().split(",")[0];
        String[] date = time.split(" ")[0].split("-");
        int w = DateUtil.getWeek(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        int weeks;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Integer.parseInt(date[1]) > 1 && Integer.parseInt(date[1]) < 8) {
            int year = Integer.parseInt(date[0]);
            int gap = DateUtil.getWeek(year, 2, 25);
            int d;
            if (gap > 3) {
                d = 25 + gap;
            } else {
                d = 25 - gap;
            }
            Date std = df.parse(year + "-02-" + d + " 00:00:00");
            Date now = df.parse(time.substring(0, 10) + " 00:00:00");
            long diff = now.getTime() - std.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            weeks = (int) days / 7 + 1;
        } else {
            int year = Integer.parseInt(date[0]);
            if (Integer.parseInt(date[1]) < 2) {
                year -= 1;
            }
            int gap = DateUtil.getWeek(year, 8, 27);
            int d;
            if (gap > 3) {
                d = 27 + gap;
            } else {
                d = 27 - gap;
            }
            Date std = df.parse(year + "-08-" + d + " 00:00:00");
            Date now = df.parse(time.substring(0, 10) + " 00:00:00");
            long diff = now.getTime() - std.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            weeks = (int) days / 7 + 1;
        }
        String[] week = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        exam.setDate(time.substring(0, 10) + "（第" + weeks + "周 " + " " + week[w - 1] + "）");

        examMapper.update(exam);
    }

    public void delete(String cid) {
        examMapper.delete(cid);
    }
}
