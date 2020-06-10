package com.wangxiaoxi.mheal.service;

import com.wangxiaoxi.mheal.entity.Admin;
import com.wangxiaoxi.mheal.entity.Document;
import com.wangxiaoxi.mheal.entity.Music;
import com.wangxiaoxi.mheal.entity.Video;
import com.wangxiaoxi.mheal.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    AdminMapper adminMapper;

    public Admin getAdminById(String id){
        return adminMapper.getAdminById(id);
    }

    public List<Admin> findAll() {
        return adminMapper.getAdminAll();
    }

    public Admin adminChecked(Admin admin) {
        return adminMapper.adminChecked(admin);
    }

    public Admin adminChecked1(Admin admin) {
        return adminMapper.adminChecked1(admin);
    }

    public void insertAdmin(Admin admin) {
        adminMapper.insertAdmin(admin);
    }
}
