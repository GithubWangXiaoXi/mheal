package com.wangxiaoxi.mheal.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ChatMsg {
    private String senduserid;

    private String reciveuserid;

    private Date sendtime;

    private String msgtype;

    private String sendtext;

}