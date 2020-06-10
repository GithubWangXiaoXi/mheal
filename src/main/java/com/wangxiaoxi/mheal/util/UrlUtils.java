package com.wangxiaoxi.mheal.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UrlUtils {

    public static boolean  testUrlWithTimeOut(String urlString,int timeOutMillSeconds){
        long lo = System.currentTimeMillis();
        URL url;
        try {
            url = new URL(urlString);
            URLConnection co =  url.openConnection();
            co.setConnectTimeout(timeOutMillSeconds);
            co.connect();
            System.out.println("连接可用");
            return true;
        } catch (Exception e1) {
            System.out.println("连接打不开!");
            url = null;
            return false;
        }
    }

    /**
     * 获取重定向地址
     * @param path
     * @return
     * @throws Exception
     */
    public static String getRedirectUrl(String path) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(path)
                .openConnection();
        //设置为不对http链接进行重定向处理
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);

        //得到请求头的所有属性和值
        Map<String, List<String>> map = conn.getHeaderFields();
        Set<String> stringSet = map.keySet();
        for (String str: stringSet){
            System.out.println(str + "------" + conn.getHeaderField(str));
        }
        //返回重定向的链接
        return conn.getHeaderField("Location");
    }
}
