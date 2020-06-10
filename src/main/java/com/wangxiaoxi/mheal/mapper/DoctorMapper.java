package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.entity.Doctor;
import com.wangxiaoxi.mheal.entity.Student;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import javax.print.Doc;
import java.util.List;

@Mapper
public interface DoctorMapper {

    //查出所有
    @Select("select * from doctor where id = #{id}")
    public Doctor getDoctorById(String id);

    //查出id + username
    @Select("select id, username from doctor where id = #{id}")
    public Doctor getDoctorById1(String id);

    @Select("select username from doctor where id = #{id}")
    public String getNameByDId(String id);

    @Insert("insert into doctor(id,username,gender,age,teachYear,graduatedSchool,tel,email,password) values (#{id},#{username}, #{gender}, #{age},#{teachYear}, #{graduatedSchool}, #{tel},#{email},#{password})")
    public void insertDoctor(Doctor doctor);

    @Update("update doctor set email = #{email} where id = #{id}")
    public void updateDoctor(Doctor doctor);

    @Delete("delete from doctor where id = #{id}")
    public void deleteDoctor(String id);

    @Select("select * from doctor")
    List<Doctor> getDoctorAll();

    @Select("select * from doctor where id = #{id}")
    Doctor doctorChecked1(Doctor doctor);

    @Select("select * from doctor where id = #{id} and password = #{password}")
    Doctor doctorChecked(Doctor doctor);

    @Select("select id from doctor")
    List<String> getDoctorIds();

    @Select("select id, username from doctor where id = #{id}")
    Doctor getDoctorByIdWithName(String id);
}
