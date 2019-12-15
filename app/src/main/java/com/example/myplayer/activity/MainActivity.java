package com.example.myplayer.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RadioGroup;

import com.example.myplayer.R;
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
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission(this);
    }

    private void startLoad(){
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

            Fragment fragment = new ReplaceFragment(getBasePager());//替换页面
            ft.replace(R.id.fl_main , fragment);
            ft.commit();                                            //提交
        }

        /**
         *  TODO:获取对应页面的资源
         * @return 对应页面
         */

        private BasePager getBasePager() {
            BasePager basePager = basePagers.get(position);
            if (!basePager.isInitData){
                basePager.isInitData = true;
                basePager.initData();
            }
            return basePager;
        }
    }



    /**
     * TODO:获取存储权限
     * @param activity
     */
    private void getPermission(Activity activity){
        String[] requestPermission = new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.INTERNET"};
        int permission = ActivityCompat.checkSelfPermission(activity,STORAGE_SERVICE);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, requestPermission, 1);
        }else{
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
