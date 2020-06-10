package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.bean.ChatMsg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMsgMapper {
    //插入聊天记录
    @Insert("insert into chat_msg (senduserid, reciveuserid,  msgtype, sendtext)\n" +
            "    values (#{senduserid}, #{reciveuserid}, #{msgtype}, #{sendtext})")
    void InsertChatMsg(ChatMsg chatMsg);
    //查询聊天记录
    @Select("select * from chat_msg where\n" +
            "    senduserid=#{chatMsg.senduserid} and reciveuserid=#{chatMsg.reciveuserid} or\n" +
            "    senduserid=#{chatMsg.reciveuserid} and reciveuserid=#{chatMsg.senduserid}")
    List<ChatMsg>  LookTwoUserMsg(@Param("chatMsg") ChatMsg chatMsg);
}