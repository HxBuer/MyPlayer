package com.example.myplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myplayer.IMyAudioService;
import com.example.myplayer.R;
import com.example.myplayer.service.AudioService;
import com.example.myplayer.utils.TimeUtils;

import java.util.ArrayList;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/15
 * TODO:音乐播放器
 */
public class MyAudioPlayerActivity extends Activity implements View.OnClickListener{

    private static final int PROGRESS = 1;
    private TimeUtils utils;
    private TextView tvAudioName;
    private TextView tvArtistName;
    private ImageView ivDown;
    private TextView tvCurrentTime;
    private TextView tvAudioDuration;
    private SeekBar seekbarAudio;
    private Button btAudioMode;
    private Button btAudioBack;
    private Button btAudioPause;
    private Button btAudioNext;
    private Button btAudioLyric;
    private MyReceiver receiver;                    //广播接收
    private int position;                           //当前播放音乐位置
    private IMyAudioService iMyAudioService;        //Service代理
    private ServiceConnection serviceConnection = new ServiceConnection() {     //服务连接
        /**
         * TODO:当 Activity与 Service连接成功时回调此方法
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAudioService = IMyAudioService.Stub.asInterface(service);
            //绑定服务成功后，操作服务
            Log.i("", "onServiceConnected: 与Service连接成功！");
            try {
                iMyAudioService.openAudio(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        /**
         * TODO:当连接断开时回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iMyAudioService = null;
            Log.i("", "onServiceDisconnected: 与Service断开连接！");
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:          //更新时间
                    try {
                        int currentPosition = iMyAudioService.getCurrentPosition();
                        int duration = iMyAudioService.getDuration();
                        tvCurrentTime.setText(utils.msToMS(currentPosition));
                        tvAudioDuration.setText(utils.msToMS(duration));
                        //更新进度
                        seekbarAudio.setProgress(iMyAudioService.getCurrentPosition());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }



                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();
        findViews();                //绑定控件，设置事件监听器
        getData();                  //获取音频位置
        bindStartService();         //绑定并启动服务
    }

    /**
     * TODO：注册广播接收器
     */
    private void initReceiver() {
        utils = new TimeUtils();
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AudioService.OPEN_AUDIO);        //监听打开音乐成功的动作
        registerReceiver(receiver, intentFilter);
    }

    class MyReceiver extends BroadcastReceiver{
        /**
         * TODO：对收到AudioService发送的广播进行处理，
         *       获取音频的名称和歌手名
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            //主线程
            setViewData();
            //发送消息
            try {
                seekbarAudio.setMax(iMyAudioService.getDuration());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(PROGRESS);

        }
    }

    private void setViewData() {
        try {
            tvAudioName.setText(iMyAudioService.getAudioName());
            tvArtistName.setText(iMyAudioService.getArtistName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void getData() {
        position = getIntent().getIntExtra("position",0);
    }

    private void bindStartService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction("com.example.myplayer.AUDIOSERVICE");
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);    //（绑定）与服务建立连接，回调
        startService(intent);                                               //避免Service被重新创建
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null){
            unbindService(serviceConnection);       //取消绑定
            serviceConnection = null;
        }

        if(receiver != null){
            unregisterReceiver(receiver);           //注销广播
            receiver = null;
        }
    }

    /**
     * TODO：关联xml，绑定控件ID，设置监听
     */
    private void findViews() {
        setContentView(R.layout.activity_audioplayer);
        tvAudioName = findViewById( R.id.tv_audio_name );
        tvArtistName = findViewById( R.id.tv_artist_name );
        ivDown = findViewById( R.id.iv_down);
        tvCurrentTime = findViewById( R.id.tv_current_time );
        tvAudioDuration = findViewById( R.id.tv_audio_duration );
        seekbarAudio = findViewById( R.id.seekbar_audio );
        btAudioMode = findViewById( R.id.bt_audio_mode );
        btAudioBack = findViewById( R.id.bt_audio_back );
        btAudioPause = findViewById( R.id.bt_audio_pause );
        btAudioNext = findViewById( R.id.bt_audio_next );
        btAudioLyric = findViewById( R.id.bt_audio_lyric );

        btAudioMode.setOnClickListener( this );
        btAudioBack.setOnClickListener( this );
        btAudioPause.setOnClickListener( this );
        btAudioNext.setOnClickListener( this );
        btAudioLyric.setOnClickListener( this );

        seekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    try {
                        iMyAudioService.seekTo(progress);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        try {
            if (v == btAudioMode) {
                // Handle clicks for btAudioMode
            } else if ( v == btAudioBack ) {
                // Handle clicks for btAudioBack
            } else if ( v == btAudioPause ) {
                if (iMyAudioService.isPlaying()){
                    iMyAudioService.pause();
                    btAudioPause.setBackgroundResource(R.drawable.play);
                }else{
                    iMyAudioService.start();
                    btAudioPause.setBackgroundResource(R.drawable.pause);
                }
            } else if ( v == btAudioNext ) {
                // Handle clicks for btAudioNext
            } else if ( v == btAudioLyric ) {
                // Handle clicks for btAudioLyric
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
