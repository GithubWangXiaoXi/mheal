package com.wangxiaoxi.mheal.util;

import org.junit.Test;

public class TestUtil {
    @Test
    public void testA(){
        String s = Md5Util.StringInMd5("123456");
        System.out.println(s);
    }
}
