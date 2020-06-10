package com.wangxiaoxi.mheal.controller;

import com.wangxiaoxi.mheal.entity.*;
import com.wangxiaoxi.mheal.service.AdminService;
import com.wangxiaoxi.mheal.service.QuesService;
import com.wangxiaoxi.mheal.service.ResourcesService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private QuesService quesService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ResourcesService resourcesService;

    /**
     * 跳转到主管理员主页
     * @param admin
     * @return
     */
    @GetMapping("/admin/toHomePage")
    public String toHomePage(Admin admin,HttpServletRequest request){
        System.out.println("主页");
        admin = adminService.getAdminById(admin.getId());
        request.getSession().setAttribute("admin",admin);
        System.out.println(admin);
        return "/admin/home";
    }

    /**
     * 跳转到登录页面
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/admin")
    public String loginPage(HttpServletRequest httpServletRequest){
        String s = httpServletRequest.getParameter("success");
        httpServletRequest.setAttribute("success",s);
        return "/user/sign-in-admin";
    }

    /**
     * 跳转到注册页面
     * @return
     */
    @GetMapping("/admin1")
    public String registerPage(){
        return "/user/sign-up-admin";
    }

    /**
     * 重定向到注册页面
     * @return
     */
    @GetMapping("/admin/toReg")
    public String toRegisterPage(){
        return "redirect:/admin1";
    }

    /**
     * 完成注册，跳转到登录页面
     * @param admin
     * @return
     */
    @PostMapping("/admin/register")
    public void register(Admin admin){
        System.out.println("提交数据" + admin);
        adminService.insertAdmin(admin);
    }

    /**
     * 校验管理员身份（id）
     * @return
     */
    @GetMapping("/admin/adminChecked1")
    @ResponseBody
    public Admin adminChecked1(Admin admin){
        System.out.println(admin.getId());
        return adminService.adminChecked1(admin);
    }

    /**
     * 校验管理员身份（id + password）
     * @return
     */
    @GetMapping("/admin/adminChecked")
    @ResponseBody
    public Admin AdminChecked(Admin admin){
        System.out.println(admin.getId()+ " ------- " + admin.getPassword());
        return adminService.adminChecked(admin);
    }


    /**
    * @Description: 跳转到问答社区
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/1 0001
    */
    @GetMapping("/admin/toAnsHood")
    public String toAnsHood(HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        System.out.println("toAnsHood " + admin);
        request.setAttribute("admin",admin);
        return "/admin/answerHood";
    }

    /**
    * @Description: 跳转到用户管理页面
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/3 0003
    */
    @GetMapping("/admin/toUserManager")
    public String toUserManager(HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        System.out.println("toUserManager " + admin);
        request.setAttribute("admin",admin);
        return "/admin/userManager";
    }

    /**
     * @Description: 跳转到资源管理页面
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/3 0003
     */
    @GetMapping("/admin/toResourceManager")
    public String toResourceManager(HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        System.out.println("toResourceManager " + admin);
        request.setAttribute("admin",admin);
        return "/admin/resourceManager";
    }

    /**
    * @Description: 跳转到活动页面
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/3 0003
    */
    @GetMapping("/admin/toAcitvities")
    public String toActivityPage(HttpServletRequest request){
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        System.out.println("toAcitvities " + admin);
        request.setAttribute("admin",admin);
        return "/admin/activities";
    }

    /**
    * @Description: 跳转到上传专栏页
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/29 0029
    */
    @GetMapping("/admin/toDocumentUpload")
    public String toDocumentUpload(){
        return "/admin/document-upload";
    }

    //上传专栏
    @PostMapping("/admin/postFile")
    public void postFile(HttpServletRequest request) throws IOException {
        //得到专栏标题
        String title = request.getParameter("title");
        //得到专栏文件
        String filePath = request.getParameter("href");
        //得到专栏标签
        String tags = request.getParameter("tags");

        Document document = new Document();
        document.setTitle(title);
        document.setContent(FileUtils.readFileToString(new File(filePath),"utf-8"));

        Tag tag = new Tag();
        String token[] = tags.split(",");
        List<Tag> tagList = new ArrayList<>();
        for (String t:token){
            tag.setId(t);
            tagList.add(tag);
        }
        document.setTags(tagList);

        resourcesService.insertDocument(document);
    }

    /**
     * @Description: 跳转到视频上传页
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/29 0029
     */
    @GetMapping("/admin/toVideoUpload")
    public String toVideoUpload(){
        return "/admin/video-upload";
    }

    //上传视频
    @PostMapping("/admin/postVideo")
    public void postVideo(HttpServletRequest request) throws IOException {
        //得到视频标题
        String title = request.getParameter("title");
        //得到视频文件
        String href = request.getParameter("href");
        //得到视频标签
        String tags = request.getParameter("tags");
        //得到视频作者
        String author = request.getParameter("author");

        Video video = new Video();
        video.setHref(href);
        video.setTitle(title);
        video.setAuthor(author);

        Tag tag = new Tag();
        String token[] = tags.split(",");
        List<Tag> tagList = new ArrayList<>();
        for (String t:token){
            tag.setId(t);
            tagList.add(tag);
        }
        video.setTags(tagList);

        resourcesService.insertVideo(video);
    }

    /**
     * @Description: 跳转到音乐上传页
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/29 0029
     */
    @GetMapping("/admin/toMusicUpload")
    public String toMusicUpload(){
        return "/admin/music-upload";
    }

    //上传视频
    @PostMapping("/admin/postMusic")
    public void postMusic(HttpServletRequest request) throws IOException {
        //得到音乐标题
        String title = request.getParameter("title");
        //得到音乐原链接
        String href = request.getParameter("href");
        //得到音乐实际链接
        String bgm= request.getParameter("bgm");
        //得到音乐标签
        String tags = request.getParameter("tags");
        //得到音乐作者
        String author = request.getParameter("author");

        Music music = new Music();
        music.setHref(href);
        music.setBgm(bgm);
        music.setTitle(title);
        music.setAuthor(author);

        Tag tag = new Tag();
        String token[] = tags.split(",");
        List<Tag> tagList = new ArrayList<>();
        for (String t:token){
            tag.setId(t);
            tagList.add(tag);
        }
        music.setTags(tagList);

        resourcesService.insertMusic(music);
    }

    @GetMapping("/admin/toActivitiesUpload")
    public String toActivitiesUpload(){
        return "/admin/activities-upload";
    }
}
