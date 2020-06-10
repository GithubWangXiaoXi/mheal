package com.wangxiaoxi.mheal.service;

import com.wangxiaoxi.mheal.bean.Login;
import com.wangxiaoxi.mheal.bean.Userinfo;
import com.wangxiaoxi.mheal.entity.Doctor;
import com.wangxiaoxi.mheal.entity.Student;
import com.wangxiaoxi.mheal.mapper.DoctorMapper;
import com.wangxiaoxi.mheal.mapper.StudentMapper;
import com.wangxiaoxi.mheal.service.cacheService.UserCacheService;
import com.wangxiaoxi.mheal.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    LoginService loginService;

    public Doctor getDoctorById(String id){
        return doctorMapper.getDoctorById(id);
    }

    public Doctor getDoctorByIdWithName(String id){
        return doctorMapper.getDoctorByIdWithName(id);
    }

    public List<Doctor> findAll() {
        return doctorMapper.getDoctorAll();
    }

    public Doctor doctorChecked(Doctor Doctor) {
        return doctorMapper.doctorChecked(Doctor);
    }

    public Doctor doctorChecked1(Doctor Doctor) {
        return doctorMapper.doctorChecked1(Doctor);
    }

    public void insertDoctor(Doctor Doctor) {
        doctorMapper.insertDoctor(Doctor);
    }

    //得到数据库中所有doctorIds
    public List<String> getDoctorIds(){
        return doctorMapper.getDoctorIds();
    }

    //得到数据库中所有studentIds
    public List<String> getStudentIds() {
        return studentMapper.getStudentIds();
    }

    public List<Doctor> getDoctorsOffline() {
        List<Doctor> doctorsOffline = new ArrayList<>();

        List<String> doctorIds = getDoctorIds();
        List<String> online = userCacheService.getDoctorsIds();
        for(String id : doctorIds){
            if(!online.contains(id)){
                doctorsOffline.add(getDoctorById(id));
            }
        }

        return doctorsOffline;
    }

    public List<Student> getStudentsOffline() {
        List<Student> studentsOffline = new ArrayList<>();

        List<String> studentIds = getStudentIds();
        List<String> online = userCacheService.getStudentsIds();
        for(String id : studentIds){
            if(!online.contains(id)){
                studentsOffline.add(studentMapper.getStuById(id));
            }
        }

        return studentsOffline;
    }

    public void insertLogin(Doctor doctor) {

        //插入login
        Login login = new Login();
        login.setUserid(doctor.getId());
        login.setUsername(doctor.getId());
        login.setPassword(Md5Util.StringInMd5(doctor.getPassword()));
        System.out.println(login);
        loginService.InsertLogin(login);

        //插入userInfo
        Userinfo userinfo = new Userinfo();
        userinfo.setNickname(doctor.getUsername());
        userinfo.setUserid(doctor.getId());
        System.out.println(userinfo);
        loginService.insertUserInfo(userinfo);
    }
}
