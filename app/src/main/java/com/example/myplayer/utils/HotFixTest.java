package com.example.myplayer.utils;

import android.util.Log;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2020/4/6
 * TODO:
 */
public class HotFixTest {
    public static void test(){
        //Log.i("HotFixTest", "test: 修复成功！");
        throw new IllegalArgumentException("异常测试···");
    }
}
