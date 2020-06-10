package com.wangxiaoxi.mheal.service;

import com.wangxiaoxi.mheal.bean.Login;
import com.wangxiaoxi.mheal.bean.Userinfo;
import com.wangxiaoxi.mheal.entity.Student;
import com.wangxiaoxi.mheal.mapper.StudentMapper;
import com.wangxiaoxi.mheal.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StuService {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    LoginService loginService;

    public Student getStuById(String id){
        return studentMapper.getStuById(id);
    }

    public Student getStuByIdWithName(String id){
        return studentMapper.getStuByIdWithName(id);
    }

    public List<Student> findAll() {
        return studentMapper.getStuAll();
    }

    public Student stuChecked(Student student) {
        return studentMapper.stuChecked(student);
    }

    public Student stuChecked1(Student student) {
        return studentMapper.stuChecked1(student);
    }

    public void insertStu(Student student) {
        studentMapper.insertStu(student);
    }

    public void insertLogin(Student student) {

        //插入login
        Login login = new Login();
        login.setUserid(student.getId());
        login.setUsername(student.getId());
        login.setPassword(Md5Util.StringInMd5(student.getPassword()));
        System.out.println(login);
        loginService.InsertLogin(login);

        //插入userInfo
        Userinfo userinfo = new Userinfo();
        userinfo.setNickname(student.getUsername());
        userinfo.setUserid(student.getId());
        System.out.println(userinfo);
        loginService.insertUserInfo(userinfo);
    }
}
