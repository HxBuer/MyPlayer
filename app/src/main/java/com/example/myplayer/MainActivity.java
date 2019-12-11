package com.example.myplayer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.myplayer.base.BasePager;
import com.example.myplayer.pager.AudioPager;
import com.example.myplayer.pager.NetVideoPager;
import com.example.myplayer.pager.PersonPager;
import com.example.myplayer.pager.ReplaceFragment;
import com.example.myplayer.pager.VideoPager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private ArrayList<BasePager> basePagers;
    private int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroup rg_main = findViewById(R.id.rg_main);

        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));
        basePagers.add(new AudioPager(this));
        basePagers.add(new NetVideoPager(this));
        basePagers.add(new PersonPager(this));

        //监听器
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_main.check(R.id.rb_vedio);
    }

    /**
     * 按键监听
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_person:
                    position = 3;
                    break;
            }

            setFragment();
        }

        private  void setFragment() {
            FragmentManager fm = getSupportFragmentManager();       //得到fragmentManager
            FragmentTransaction ft = fm.beginTransaction();         //开启事务

            Fragment fragment = new ReplaceFragment(getBasePager());
            ft.replace(R.id.fl_main , fragment);
            ft.commit();                                            //提交
        }

        private BasePager getBasePager() {
            BasePager basePager = basePagers.get(position);
            if (!basePager.isInitData){
                basePager.isInitData = true;
                basePager.initData();
            }
            return basePager;
        }
    }
}
