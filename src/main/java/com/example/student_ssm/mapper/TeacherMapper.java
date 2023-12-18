package com.example.student_ssm.mapper;

import com.example.student_ssm.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {

    @Select("select * from teacher where number = #{number}")
    Teacher teacherSelectByNumber(String number);
    // TODO: course 模式

    @Select("select * from teacher where username like concat('%', #{username}, '%') and number like concat('%', #{number}, '%')")
    List <Teacher> teacherSelectBy(Teacher teacher);

    @Select("select * from teacher")
    List<Teacher> selectAll();

    @Select("select * from teacher where username like concat('%', #{name}, '%')")
    List<Teacher> selectByName(String name);

    @Insert("insert into teacher(number, username, gender, phone, email, major) " +
            "values(#{number}, #{username}, #{gender}, #{phone}, #{email}, #{major})")
    int insert(Teacher teacher);

    @Update("update teacher set username = #{username}, gender = #{gender}, phone = #{phone}, email = #{email}, major = #{major} " +
            "where number = #{number}")
    int update(Teacher teacher);

    @Update("update teacher set password = #{password} where number = #{number}")
    int updatePassword(Teacher teacher);

    @Delete("delete from teacher where number = #{number}")
    int delete(String number);
}
