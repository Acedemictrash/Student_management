package com.example.student_ssm.mapper;

import com.example.student_ssm.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("select * from admin where number = #{number}")
    Admin adminSelectByNumber(String number);

}
