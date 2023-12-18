package com.example.student_ssm.mapper;

import com.example.student_ssm.entity.Exam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamMapper {

    @Select("select sc.cid, c.name, e.date, e.introduction, e.type, e.other from selectCourse as sc " +
            "left join course as c on sc.cid = c.id left join exam as e on e.cid = sc.cid where sid = #{id}")
    List<Exam> examSelect(String id);

    @Select("select * from exam where cid like concat('%', #{cid}, '%') and name like concat('%', #{name}, '%')")
    List<Exam> examSelectAll(String cid, String name);

    @Insert("insert into exam(cid, name, date, introduction, type, other) values(#{cid}, #{name}, #{date}, #{introduction}, #{type}, #{other})")
    int insert(Exam exam);

    @Update("update exam set date = #{date}, introduction = #{introduction}, type = #{type}, other = #{other} where cid = #{cid}")
    int update(Exam exam);

    @Delete("delete from exam where cid = #{cid}")
    int delete(String cid);
}
