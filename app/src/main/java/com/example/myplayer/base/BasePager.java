package com.example.myplayer.base;

import android.content.Context;
import android.view.View;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/9
 * TODO:本地视频，本地音乐，网络视频的基类
 *
 */
public abstract class BasePager {

    protected Context context;         //上下文
    public View rootView;           //视图
    public boolean isInitData = false;      //表示是否初始化

    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    /**
     * 子类需要重写，用来创建View
     * @return View
     */
    public abstract View initView();

    public abstract void initData();
}

