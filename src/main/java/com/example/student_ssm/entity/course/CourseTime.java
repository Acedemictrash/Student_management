package com.example.student_ssm.entity.course;

import lombok.Data;

import java.util.Objects;

@Data
public class CourseTime {

    private String cid;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
    private int startWeek;
    private int endWeek;
    private String startTime;
    private String endTime;

    public CourseTime(int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, String startTime, String endTime, int startWeek, int endWeek) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
    }

    public CourseTime() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseTime that = (CourseTime) o;
        return monday == that.monday && tuesday == that.tuesday && wednesday == that.wednesday && thursday == that.thursday && friday == that.friday && saturday == that.saturday && sunday == that.sunday && startWeek == that.startWeek && endWeek == that.endWeek && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monday, tuesday, wednesday, thursday, friday, saturday, sunday, startWeek, endWeek, startTime, endTime);
    }
}
