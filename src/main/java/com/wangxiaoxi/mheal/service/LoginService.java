package com.wangxiaoxi.mheal.service;

import com.wangxiaoxi.mheal.bean.Login;
import com.wangxiaoxi.mheal.bean.Userinfo;
import com.wangxiaoxi.mheal.entity.Doctor;
import com.wangxiaoxi.mheal.entity.Student;
import com.wangxiaoxi.mheal.mapper.LoginMapper;
import com.wangxiaoxi.mheal.util.Md5Util;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    LoginMapper loginMapper;
    public String justLogin(Login login){
        return loginMapper.justLogin(login);
    }
    public String lkUseridByUsername(String username){
        return loginMapper.lkUseridByUsername(username);
    }
    public void InsertLogin(Login login){
        loginMapper.insertLogin(login);
    }
    public void insertUserInfo(Userinfo userinfo){
        loginMapper.insertUserInfo(userinfo);
    }

    public Login getLoginFromStu(Student student) {
        Login login = new Login();
        login.setUserid(student.getId());
        login.setUsername(student.getId());
        login.setPassword(Md5Util.StringInMd5(student.getPassword()));
        return login;
    }

    public Login getLoginFromDoctor(Doctor doctor) {
        Login login = new Login();
        login.setUserid(doctor.getId());
        login.setUsername(doctor.getId());
        login.setPassword(Md5Util.StringInMd5(doctor.getPassword()));
        return login;
    }
}
