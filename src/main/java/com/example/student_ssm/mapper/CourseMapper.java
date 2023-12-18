package com.example.student_ssm.mapper;

import com.example.student_ssm.entity.VO.SelectVO;
import com.example.student_ssm.entity.course.SelectCourse;
import com.example.student_ssm.entity.course.CourseReturn;
import com.example.student_ssm.entity.VO.CourseVO;
import com.example.student_ssm.entity.course.CourseTime;
import com.example.student_ssm.entity.course.CourseToDb;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Select("select c.id, c.name, c.credit, ct.startWeek, ct.endWeek, c.classroom, c.maxNum, c.currentNum, c.introduction, " +
            "t.username, ct.monday, ct.tuesday, ct.wednesday, ct.thursday, ct.friday, ct.saturday, ct.sunday, ct.startTime, ct.endTime, sc.weight " +
            "from course as c left join teacher as t on c.tid = t.number left join courseTime as ct on c.id = ct.cid left join selectCourse as sc on c.id = sc.cid" +
            " where c.name like concat('%', #{name}, '%') and c.id like concat('%', #{id}, '%') and t.username like concat('%', #{username}, '%') and sc.sid like concat('%', #{sid}, '%')")
    List<CourseReturn> selectCourseBy(CourseVO courseVO);

    @Select("select c.id, c.name, c.credit, ct.startWeek, ct.endWeek, c.classroom, c.maxNum, c.currentNum, c.introduction, " +
            "t.username, ct.monday, ct.tuesday, ct.wednesday, ct.thursday, ct.friday, ct.saturday, ct.sunday, ct.startTime, ct.endTime, sc.weight " +
            "from course as c left join teacher as t on c.tid = t.number left join courseTime as ct on c.id = ct.cid left join selectCourse as sc on c.id = sc.cid" +
            " where c.name like concat('%', #{name}, '%') and c.id like concat('%', #{id}, '%') and t.username like concat('%', #{username}, '%') " +
            "and not exists(select * from selectCourse where sc.sid = #{sid} and sc.cid = c.id)")
    List<CourseReturn> selectCourseNotBy(CourseVO courseVO);

    @Select("select c.id, c.name, c.credit, ct.startWeek, ct.endWeek, c.classroom, c.maxNum, c.currentNum, c.introduction, " +
            "t.username, ct.monday, ct.tuesday, ct.wednesday, ct.thursday, ct.friday, ct.saturday, ct.sunday, ct.startTime, ct.endTime, sc.weight " +
            "from course as c left join teacher as t on c.tid = t.number left join courseTime as ct on c.id = ct.cid left join selectCourse as sc on c.id = sc.cid")
    List<CourseReturn> selectCourse();

    @Insert("insert into course(id, name, credit, tid, classroom, maxNum, currentNum, introduction) values(#{id}, #{name}, #{credit}, #{tid}, #{classroom}, #{maxNum}, #{currentNum}, #{introduction})")
    int insertCourse(CourseToDb course);

    @Insert("insert into courseTime(cid, monday, tuesday, wednesday, thursday, friday, saturday, sunday, startTime, endTime, startWeek, endWeek) " +
            "values(#{cid}, #{monday}, #{tuesday}, #{wednesday}, #{thursday}, #{friday}, #{saturday}, #{sunday}, #{startTime}, #{endTime}, #{startWeek}, #{endWeek})")
    int insertCourseTime(CourseTime courseTime);

    @Update("update course set name = #{name}, credit = #{credit}, tid = #{tid}, classroom = #{classroom}, maxNum = #{maxNum}, currentNum = #{currentNum}, introduction = #{introduction} " +
            "where id = #{id}")
    int updateCourse(CourseToDb course);

    @Delete("delete from courseTime where cid = #{id}")
    int deleteCourseTime(String id);

    @Delete("delete from course where id = #{id}")
    int deleteCourse(String id);

    @Insert("insert into selectCourse(sid, cid, tid, weight) values(#{sid}, #{cid}, #{tid}, #{weight})")
    int addSelect(SelectCourse selectCourse);

    @Update("update course set currentNum = currentNum + 1 where id = #{cid}")
    int addCurrentNum(String cid);

    @Update("update student set leftWeight = leftWeight - #{weight} where number = #{sid}")
    int addStudentSelect(SelectCourse selectCourse);

    @Select("select * from selectCourse where sid = #{sid} and cid = #{cid}")
    List<SelectCourse> getSelect(SelectCourse selectCourse);

    @Update("update selectCourse set weight = #{weight} where sid = #{sid} and cid = #{cid}")
    int updateSelect(SelectCourse selectCourse);

    @Delete("delete from selectCourse where sid = #{sid} and cid = #{cid}")
    int deleteSelect(SelectCourse selectCourse);

    @Update("update course set currentNum = currentNum - 1 where id = #{cid}")
    int deleteCurrentNum(String cid);

    @Select("select sc.sid, s.username, sc.weight, sc.grade from selectCourse as sc left join student as s on sc.sid = s.number where cid = #{cid} order by weight desc")
    List<SelectVO> selectCoursePage(String cid);

    @Update("update selectCourse set grade = #{grade} where sid = #{sid} and cid = #{cid}")
    int updateGrade(SelectCourse selectCourse);
}
