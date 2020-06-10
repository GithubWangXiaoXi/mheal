package com.wangxiaoxi.mheal.controller;

import com.alibaba.fastjson.JSONObject;
import com.wangxiaoxi.mheal.bean.ChatFriends;
import com.wangxiaoxi.mheal.bean.ChatMsg;
import com.wangxiaoxi.mheal.service.ChatFriendsService;
import com.wangxiaoxi.mheal.service.ChatMsgService;
import com.wangxiaoxi.mheal.service.LoginService;
import com.wangxiaoxi.mheal.util.EmojiFilter;
import com.wangxiaoxi.mheal.util.exception.R;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class ChatCtrl {
    @Autowired
    ChatFriendsService chatFriendsService;
    @Autowired
    ChatMsgService chatMsgService;
    @Autowired
    LoginService loginService;

    /**
     * 上传聊天图片
     * **/
    @PostMapping(value = "/chat/upimg")
    @ResponseBody
    public JSONObject upauz(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());//获得文件扩展名
        String filenames = filename + "." + ext;
        file.transferTo(new File("D:\\chat\\" + filenames));
        resUrl.put("src", "/pic/" + filenames);
        res.put("msg", "");
        res.put("code", 0);
        res.put("data", resUrl);
        return res;
    }
    /**
     * 添加好友：查询用户
     * */
    @PostMapping("/chat/lkuser/{username}")
    @ResponseBody
    public R lkuser(@PathVariable("username")String username){
        username= EmojiFilter.filterEmoji(username);
        String uid = loginService.lkUseridByUsername(username);
        if(uid==null){
            return R.error().message("未查询到此用户");
        }
        return R.ok().data("userinfo",chatFriendsService.LkUserinfoByUserid(uid)).message("用户信息");
    }
    /**
     * 添加好友
     * */
    @PostMapping("/chat/adduser/{fuserid}")
    @ResponseBody
    public R tofuseridchat(@PathVariable("fuserid")String fuserid, HttpSession session){
        String userid=(String)session.getAttribute("userid");
        if(userid.equals(fuserid)){
            return R.error().message("不能添加自己为好友");
        }
        ChatFriends chatFriends=new ChatFriends();
        chatFriends.setUserid(userid).setFuserid(fuserid);
        Integer integer = chatFriendsService.JustTwoUserIsFriend(chatFriends);
        if(integer==null){
            //如果不存在好友关系插入好友关系
            chatFriendsService.InsertUserFriend(chatFriends);
            chatFriendsService.InsertUserFriend(new ChatFriends().setFuserid(userid).setUserid(fuserid));
        }
        return R.ok().message("添加成功");
    }
    /**
     * 跳转到聊天
     * */
    @GetMapping("/chat/ct")
    public String tochat(){
        return "/chat/chats";
    }

    /***
     * 查询用户的好友
     * */
    @PostMapping("/chat/lkfriends")
    @ResponseBody
    public List<ChatFriends> lkfriends(HttpSession session){
        String userid=(String)session.getAttribute("userid");
        return chatFriendsService.LookUserAllFriends(userid);
    }
    /***
     * 查询两个用户之间的聊天记录
     * */
    @PostMapping("/chat/lkuschatmsg/{reviceuserid}")
    @ResponseBody
    public List<ChatMsg> lkfriends(HttpSession session, @PathVariable("reviceuserid")String reviceuserid){
        String userid=(String)session.getAttribute("userid");
        return chatMsgService.LookTwoUserMsg(new ChatMsg().setSenduserid(userid).setReciveuserid(reviceuserid));
    }
    /***
     * Ajax上传web界面js录制的音频数据
     * */
    @PostMapping("/chat/audio")
    @ResponseBody
    public JSONObject upaudio(@RequestParam(value = "file") MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String filenames = filename + ".mp3";
        String pathname = "D:\\chat\\" + filenames;
        file.transferTo(new File(pathname));
        resUrl.put("src", "/pic/"+filenames);
        res.put("msg", "");
        res.put("data", resUrl);
        return res;
    }
}
