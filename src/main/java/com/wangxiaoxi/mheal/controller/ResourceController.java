package com.wangxiaoxi.mheal.controller;

import com.wangxiaoxi.mheal.entity.Doctor;
import com.wangxiaoxi.mheal.entity.Music;
import com.wangxiaoxi.mheal.entity.Student;
import com.wangxiaoxi.mheal.entity.Video;
import com.wangxiaoxi.mheal.service.ResourcesService;
import com.wangxiaoxi.mheal.util.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: wangxiaoxi
 * @create: 2020-04-03 11:46
 **/
@Controller
public class ResourceController {


    @Autowired
    private ResourcesService resourcesService;

    /**
     * @Description: 跳转到视频区
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/3 0003
     */
    @GetMapping(value = "/video/toVideoPage")
    public String toVideoPage(HttpServletRequest request){
        String id = request.getParameter("id");
        Video video = resourcesService.getVideoById(id);
        request.setAttribute("video",video);
        return "/study/videoPage";
    }

    /**
     * @Description: 跳转到学习社区
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/3 0003
     */
    @GetMapping(value = "/study/toStudyPage")
    public String toStudyPage(HttpServletRequest request){
        return "/study/study";
    }

    /**
     * @Description: 跳转到文章区
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/3 0003
     */
    @GetMapping(value = "/document/toDocPage")
    public String toDocPage(HttpServletRequest request){
        return "/study/document";
    }

    //获取所有视频json数据
    @GetMapping("/resource/getVideoAll")
    @ResponseBody
    private List<Video> getVideoAll(){
        List<Video> videoAll = resourcesService.getVideoAll();
        return videoAll;
    }

    //判断连接是否可访问
    @GetMapping("/resource/UrlTest")
    @ResponseBody
    private Boolean isAccessible(HttpServletRequest request){

        //得到音频链接或视频链接
        String url = request.getParameter("url");

        //判断该链接是否有http，如果没有，则给该链接添加http网络协议
        if(!url.contains("http")){
            url = "http:" + url;
        }
//        System.out.println(url);
        return UrlUtils.testUrlWithTimeOut(url,2000);
    }

    //得到网易云音频链接重定向后的链接
    @GetMapping("/resource/getRedirect")
    @ResponseBody
    private String getRedirect(HttpServletRequest request) throws Exception {

        String bgm = request.getParameter("bgm");

        //判断该链接是否有http，如果没有，则给该链接添加http网络协议
        if(!bgm.contains("http")){
            bgm = "http://" + bgm;
        }
//        System.out.println(bgm);
        //得到网易云音频链接重定向后的链接
        String redirect = UrlUtils.getRedirectUrl(bgm);
        System.out.println(redirect);
        return redirect;
    }

    //获取所有音频json数据
    @GetMapping("/resource/getMusicAll")
    @ResponseBody
    private List<Music> getMusicAll(){
        List<Music> musicAll = resourcesService.getMusicAll();
        return musicAll;
    }
}
