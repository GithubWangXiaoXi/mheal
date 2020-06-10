package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.entity.Doctor;
import com.wangxiaoxi.mheal.entity.Student;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Select("select * from student where id = #{id}")
    public Student getStuById(String id);

    @Select("select username from student where id = #{s_id}")
    public String getUsernameById(String s_id);

    @Insert("insert into student(id,username,gender,age,tel,email,password) values (#{id},#{username}, #{gender}, #{age},#{tel},#{email},#{password})")
    public void insertStu(Student student);

    @Update("update student set email = #{email} where id = #{id}")
    public void updateStu(Student student);

    @Delete("delete from student where id = #{id}")
    public void deleteStu(String id);

    @Select("select id,username,password from student")
    public List<Student> getStuAll();

    @Select("select * from student where id = #{id} and password = #{password}")
    public Student stuChecked(Student student);

    @Select("select * from student where id = #{id}")
    Student stuChecked1(Student student);

    /**
    * @Description: 根据问题id查询学生
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/16 0016
    */
    @Results({
            @Result(column = "s_id",property = "id"),
            @Result(column = "s_id",property = "username",one = @One(select = "com.wangxiaoxi.mheal.mapper.StudentMapper.getUsernameById",fetchType = FetchType.DEFAULT))
    })
    @Select("select distinct s_id from askAndAns where q_id = #{q_id}")
    Student getStuByQuesId(String q_id);

    @Select("select id,username from student where id = #{id}")
    Student getStuByIdWithName(String id);

    @Select("select id from student")
    List<String> getStudentIds();
}
