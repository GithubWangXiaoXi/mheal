package com.wangxiaoxi.mheal.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Userinfo {
    private Integer uid;
    private String nickname;
    private String usign;
    private String userid;
    private String uimg;
}
