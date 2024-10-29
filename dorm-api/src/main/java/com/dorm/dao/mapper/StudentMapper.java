package com.dorm.dao.mapper;

import com.dorm.dao.model.Student;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface StudentMapper extends Mapper<Student> {

    List<Student> selectStudentList(@Param("kw") String kw, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectStudentCount(@Param("kw") String kw);

    List<Map<String, Object>> selectNewStudentCount();
}