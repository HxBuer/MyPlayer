package com.example.myplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myplayer.R;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/9
 * TODO:
 */
public class TitleBar extends LinearLayout {

    private final Context context;
    private View search;
    private View iv_history;

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        search = getChildAt(1);
        iv_history = getChildAt(2);

        //点击事件
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        search.setOnClickListener(myOnClickListener);
        iv_history.setOnClickListener(myOnClickListener);
    }
    /**
     * TODO:自定义点击事件
     */
    class MyOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_search :
                    Toast.makeText(context, "搜索" , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_history :
                    Toast.makeText(context, "播放历史" , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
