package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.bean.ChatFriends;
import com.wangxiaoxi.mheal.bean.Userinfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatFriendsMapper {
    //查询所有的好友
    @Select("select userid,nickname,uimg from userinfo where userid in (select a.fuserid from chat_friends a where a.userid=#{userid})")
    List<ChatFriends> LookUserAllFriends(String userid);
    //插入好友
    @Insert(" insert into chat_friends (userid, fuserid) value (#{userid},#{fuserid})")
    void InsertUserFriend(ChatFriends chatFriends);
    //判断是否加好友
    @Insert(" select id from chat_friends where userid=#{userid} and fuserid=#{fuserid}")
    Integer JustTwoUserIsFriend(ChatFriends chatFriends);
    //查询用户的信息
    @Select("select * from userinfo where userid=#{userid}")
    Userinfo LkUserinfoByUserid(String userid);
    //查询所有好友的id
    @Select("select fuserid from chat_friends where userid = #{userid}")
    List<String> getFriendsId(String userid);
}