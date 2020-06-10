package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.entity.Admin;
import com.wangxiaoxi.mheal.entity.Document;
import com.wangxiaoxi.mheal.entity.Music;
import com.wangxiaoxi.mheal.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("select * from admin where id = #{id}")
    public Admin getAdminById(String id);

    @Insert("insert into admin(id,username,gender,age,tel,email,password) values (#{id},#{username}, #{gender}, #{age},#{tel},#{email},#{password})")
    public void insertAdmin(Admin admin);

    @Update("update admin set email = #{email} where id = #{id}")
    public void updateAdmin(Admin admin);

    @Delete("delete from admin where id = #{id}")
    public void deleteAdmin(String id);

    @Select("select id,username,password from admin")
    public List<Admin> getAdminAll();

    @Select("select * from admin where id = #{id} and password = #{password}")
    public Admin adminChecked(Admin admin);

    @Select("select * from admin where id = #{id}")
    Admin adminChecked1(Admin admin);
}
