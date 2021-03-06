package com.example.myplayer.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RadioGroup;

import com.example.myplayer.R;
import com.example.myplayer.base.BasePager;
import com.example.myplayer.pager.AudioPager;
import com.example.myplayer.pager.NetVideoPager;
import com.example.myplayer.pager.PersonPager;
import com.example.myplayer.pager.ReplaceFragment;
import com.example.myplayer.pager.VideoPager;
import com.example.myplayer.utils.HotFixTest;
import com.example.myplayer.utils.Hotfix;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private ArrayList<BasePager> basePagers;
    private int position = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission(this);

        /*
        hotFix测试
         */
        HotFixTest.test();
    }

    /**
     * TODO：由于MainActivity设置为singleTask,onRetart()无法更新Intent，需要重写此方法以更新Intent
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * TODO:LoginActivity结束时，更新用户界面信息
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("", "onRestart: MainActivity");
        String user_data = null;
        try {
            user_data = this.getIntent().getExtras().getString("user_data");
            Log.i("user_data:", user_data);
            basePagers.get(3).initUserData(user_data);
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    /**
     * TODO：获取权限后加载Activity
     */
    private void startLoad() {
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
     * TODO：按键监听
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
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

        private void setFragment() {
            FragmentManager fm = getSupportFragmentManager();       //得到fragmentManager
            FragmentTransaction ft = fm.beginTransaction();         //开启事务
            Fragment fragment = new ReplaceFragment(getBasePager());
            ft.replace(R.id.fl_main, fragment);                    //替换页面
            ft.commit();                                            //提交
        }

        /**
         * TODO:获取对应页面的资源
         *
         * @return 对应页面
         */
        private BasePager getBasePager() {
            BasePager basePager = basePagers.get(position);
            if (position != 3 && !basePager.isInitData) {             //用户界面留给LoginActivity更新
                basePager.isInitData = true;
                basePager.initData();
            }
            return basePager;
        }
    }


    /**
     * TODO:获取存储权限
     *
     * @param activity
     */
    private void getPermission(Activity activity) {
        String[] requestPermission = new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.INTERNET"};
        int permission = ActivityCompat.checkSelfPermission(activity, STORAGE_SERVICE);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, requestPermission, 1);
        } else {
            startLoad();
        }
    }

    /**
     * TODO:获取用户权限操作后对应的处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLoad();
            } else {
//                AlertDialog dialog = new AlertDialog.Builder(this)
//                        .setTitle("提示:")
//                        .setMessage("权限不足，无法运行")
//                        .setIcon(R.mipmap.ic_launcher_round)
//                        .create();
//                dialog.show();
                this.finish();
                System.exit(0);
            }
        }
    }
}
