package com.example.myplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.myplayer.base.BasePager;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/9
 * TODO:本地音乐页面
 */
public class AudioPager extends BasePager {

    private TextView textView;

    public AudioPager(Context context){
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        return textView;
    }

    @Override
    public void initData() {
        textView.setText("本地音乐");
    }


}
