package com.wangxiaoxi.mheal.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data//get set
@Accessors(chain = true)
public class Login {
    private Integer id;
    private String userid;
    private String username;
    private String password;
}
