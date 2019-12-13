package com.example.myplayer;

import android.app.Application;

import org.xutils.x;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/13
 * TODO:
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);  // 是否输出debug日志, 开启debug会影响性能.
    }
}
