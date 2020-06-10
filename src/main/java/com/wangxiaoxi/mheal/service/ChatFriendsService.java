package com.wangxiaoxi.mheal.service;

import com.wangxiaoxi.mheal.bean.ChatFriends;
import com.wangxiaoxi.mheal.bean.Userinfo;
import com.wangxiaoxi.mheal.mapper.ChatFriendsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatFriendsService {

    @Autowired
    ChatFriendsMapper chatFriendsMapper;

    public List<ChatFriends> LookUserAllFriends(String userid){
        return chatFriendsMapper.LookUserAllFriends(userid);
    }
    public void InsertUserFriend(ChatFriends chatFriends){
        chatFriendsMapper.InsertUserFriend(chatFriends);
    }
    public Integer JustTwoUserIsFriend(ChatFriends chatFriends){
        return chatFriendsMapper.JustTwoUserIsFriend(chatFriends);
    }
    public Userinfo LkUserinfoByUserid(String userid){
        return chatFriendsMapper.LkUserinfoByUserid(userid);
    }

    public void setChatFriends(String s_id, String d_id) {

        //建立学生和老师的关系
        ChatFriends chatFriends = new ChatFriends();
        chatFriends.setUserid(s_id);
        chatFriends.setFuserid(d_id);
        chatFriendsMapper.InsertUserFriend(chatFriends);

        //建立老师和学生的关系
        chatFriends.setUserid(d_id);
        chatFriends.setFuserid(s_id);
        chatFriendsMapper.InsertUserFriend(chatFriends);
    }

    public List<String> getFriendsId(String userid){
        return chatFriendsMapper.getFriendsId(userid);
    }
}
