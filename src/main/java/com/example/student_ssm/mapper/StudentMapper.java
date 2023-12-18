package com.example.student_ssm.mapper;

import com.example.student_ssm.entity.Score;
import com.example.student_ssm.entity.Student;
import com.example.student_ssm.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Select("select * from student where number = #{number}")
    Student studentSelectByNumber(String number);

    @Select("select * from student where username like concat('%', #{username}, '%') and number like concat('%', #{number}, '%')")
    List<Student> studentSelectBy(Student student);

    @Select("select * from student")
    List<Student> selectAll();

    @Insert("insert into student(number, username, gender, phone, email, grade, GPA, credit, major, avatar) " +
            "values (#{number}, #{username}, #{gender}, #{phone}, #{email}, #{grade}, #{GPA}, #{credit}, #{major}, #{avatar})")
    int insert(Student student);

    @Update("update student set username = #{username}, gender = #{gender}, phone = #{phone}, email = #{email}, grade = #{grade}, GPA = #{GPA}, credit = #{credit}, major = #{major}, avatar=#{avatar}" +
            " where number = #{number}")
    int update(Student student);

    @Update("update student set password = #{password} where number = #{number}")
    int updatePassword(Student student);

    @Delete("delete from student where number = #{number}")
    int delete(String number);

    @Select("select t.username as teacher, s.username as username, sc.sid as number, sc.cid as id, sc.grade, c.credit, c.name from selectCourse as sc left join student as s on s.number = sc.sid " +
            "left join teacher as t on sc.tid = t.number left join course as c on sc.cid = c.id where sc.sid = #{number}")
    List<Score> selectGrade(String number);
}
