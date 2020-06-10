package com.wangxiaoxi.mheal.util;

import com.wangxiaoxi.mheal.entity.Content;

import java.util.Comparator;

/**
 * 对content内容进行排序
 * @author: wangxiaoxi
 * @create: 2020-03-27 21:06
 **/
public class CompareContentByTime implements Comparator<Content> {

    @Override
    public int compare(Content o1, Content o2) {
        String token1[] = o1.getTime().split(":");
        String token2[] = o2.getTime().split(":");

        //比小时
        if(Integer.valueOf(token1[0]) > Integer.valueOf(token2[0])){
            //大数排在后面，及按时间排序
            return 1;
        }
        //小时相等比分钟
        else if(Integer.valueOf(token1[0]) == Integer.valueOf(token2[0])){

            if(Integer.valueOf(token1[1]) > Integer.valueOf(token2[1])){
                return 1;
            }
            //分钟相等比秒
            else if (Integer.valueOf(token1[1]) == Integer.valueOf(token2[1])) {
                if(Integer.valueOf(token1[2]) > Integer.valueOf(token2[2])) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
            else {
                return -1;
            }
        }else {
            return -1;
        }
    }
}
