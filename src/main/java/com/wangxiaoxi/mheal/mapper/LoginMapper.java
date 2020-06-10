package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.bean.Login;
import com.wangxiaoxi.mheal.bean.Userinfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    //判断登录
    @Select("select userid from login where username=#{username} and password=#{password}")
    String justLogin(Login login);
    //根据账号查询用户ID
    @Select("select userid from login where username=#{username}")
    String lkUseridByUsername(String username);
    @Insert("insert into login(userid,username,password) values (#{userid},#{username},#{password})")
    void insertLogin(Login login);
    @Insert("insert into userinfo(nickname,usign,userid,uimg) values (#{nickname},#{usign},#{userid},#{uimg})")
    void insertUserInfo(Userinfo userinfo);
}
