package com.example.myplayer.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.myplayer.R;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/15
 * TODO:音乐播放器
 */
public class AudioPlayer extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audioplayer);
    }
}
