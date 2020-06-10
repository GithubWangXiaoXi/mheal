package com.wangxiaoxi.mheal.controller;

import com.wangxiaoxi.mheal.bean.Login;
import com.wangxiaoxi.mheal.service.LoginService;
import com.wangxiaoxi.mheal.util.Md5Util;
import com.wangxiaoxi.mheal.util.exception.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginCtrl {
    @Autowired
    LoginService loginService;
    @GetMapping("/")
    public String tologin(){
        return "user/login";
    }
    /**
     * 登陆
     * */
    @PostMapping("/justlogin")
    @ResponseBody
    public R login(@RequestBody Login login, HttpSession session){
        login.setPassword(Md5Util.StringInMd5(login.getPassword()));
        String userid = loginService.justLogin(login);
        if(userid==null){
            return R.error().message("账号或者密码错误");
        }
        session.setAttribute("userid",userid);
        return R.ok().message("登录成功");
    }
}
