package com.example.myplayer.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.example.myplayer.IMyAudioService;

import com.example.myplayer.domain.MediaItem;

import java.io.IOException;
import java.util.ArrayList;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/26
 * TODO:音乐播放服务
 */
public class AudioService extends Service {

    public static final String OPEN_AUDIO = "com.example.myplayer.OPEN_AUDIO";
    private ArrayList<MediaItem> mediaItems;            //音频列表
    private MediaItem mediaItem;                        //一首歌
    private int position;                               //当前音乐播放位置
    private MediaPlayer mediaPlayer;                    //播放器

    private IMyAudioService.Stub stub = new IMyAudioService.Stub() {
        //定义服务
        AudioService audioService = AudioService.this;
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            audioService.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            audioService.start();
        }

        @Override
        public void pause() throws RemoteException {
            audioService.pause();
        }

        @Override
        public void next() throws RemoteException {
            audioService.next();
        }

        @Override
        public void pre() throws RemoteException {
            audioService.pre();
        }

        @Override
        public void setPlayMode() throws RemoteException {
            audioService.setPlayMode();
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return audioService.getPlayMode();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return audioService.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return audioService.getDuration();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return audioService.getAudioName();
        }

        @Override
        public String getArtistName() throws RemoteException {
            return audioService.getArtistName();
        }

        @Override
        public void seekTo(int insert) throws RemoteException {
            audioService.seekTo(insert);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return audioService.isPlayeing();
        }
    };

    private boolean isPlayeing() {
        return mediaPlayer.isPlaying();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("AudioService->", "onBind: ");
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("AudioService->", "onCreate:");
        getData();

    }

    /**
     * TODO:获取音频数据列表
     */
    private void getData() {
        new Thread() {
            public void run() {
                mediaItems = new ArrayList<>();                          //容器
                ContentResolver contentResolver = getContentResolver();  //服务本身是Context
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;   //外部存储
                String[] objects = new String[]{
                        MediaStore.Audio.Media.DISPLAY_NAME,             //视频名称
                        MediaStore.Audio.Media.DURATION,                 //时长
                        MediaStore.Audio.Media.SIZE,                     //大小
                        MediaStore.Audio.Media.DATA,                     //绝对路径
                        MediaStore.Audio.Media.ARTIST                    //演唱者
                };
                //游标
                Cursor cursor = contentResolver.query(uri, objects, null, null, null);
                if (null != cursor) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(0);
                        long duration = cursor.getLong(1);
                        long size = cursor.getLong(2);
                        String data = cursor.getString(3);
                        String artistName = cursor.getString(4);
                        Log.i("AudioService", "info: " + name+"\n"+duration+"\n"+size+"\n"+data+"\n");
                        MediaItem mediaItem = new MediaItem(name, duration, size, data,artistName);
                        mediaItems.add(mediaItem);                       //添加到容器中
                    }
                    cursor.close();
                }
            }
        }.start();
    }

    /**
     * TODO:根据位置打开音乐
     * @param position
     */
    private void openAudio(int position){
        this.position = position;                           //有可能越界··
        Log.i("AudioService->", "openAudio: position="+position);
        if(mediaItems != null && mediaItems.size()>0){
            mediaItem = mediaItems.get(position);           //获取对应歌曲信息
            if(mediaPlayer != null){                        //释放已存在的或上一次的播放器，防止同时播放
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            try {
                mediaPlayer = new MediaPlayer();                                    //新建播放器
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());      //播放器准备状态监听
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());            //播放错误监听
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());  //播放完成监听
                mediaPlayer.setDataSource(mediaItem.getData());                     //设置音频播放路径
                mediaPlayer.prepareAsync();                                         //播放器异步准备，prepare为
                //mediaPlayer.prepare();  同步准备,可能出现5秒后无响应
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(this,"音乐正在加载中···",Toast.LENGTH_SHORT).show();
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{
        /**
         * TODO：播放器准备好时回调此方法
         * @param mp
         */
        @Override
        public void onPrepared(MediaPlayer mp) {
            start();
            notifyChange(OPEN_AUDIO);
        }
    }

    /**
     * TODO：发送广播更新歌曲和歌手的名称、歌曲的时长
     * @param action
     */
    private void notifyChange(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }
    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    /**
     * TODO:播放
     */
    private void start(){
        mediaPlayer.start();
        Log.i("AudioService->", "开始播放！");
    }

    /**
     * TODO:暂停
     */
    private void pause(){
        mediaPlayer.pause();
    }

    /**
     * TODO:下一首
     */
    private void next(){

    }

    /**
     * TODO:上一首
     */
    private void pre(){

    }

    /**
     * TODO:切换播放模式
     */
    private void setPlayMode(){
    }

    /**
     * TODO:获取播放模式
     * @return play mode
     */
    private int getPlayMode(){
        return 0;
    }

    /**
     * TODO:获取当前播放进度
     */
    private int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * TODO:得到总时长
     */
    private int getDuration(){
        return mediaPlayer.getDuration();
    }

    /**
     * TODO:获取歌曲名称
     * @return 歌曲名称
     */
    private String getAudioName(){
        return mediaItem.getName();
    }

    /**
     * TODO:获取歌手名称
     * @return 歌手名称
     */
    private String getArtistName(){
        return mediaItem.getArtistName();
    }

    /**
     * TODO:拖动
     * @param insert
     */
    private void seekTo(int insert){
        mediaPlayer.seekTo(insert);
    }


}
