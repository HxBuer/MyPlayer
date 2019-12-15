package com.example.myplayer.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myplayer.activity.MyVideoPlayer;
import com.example.myplayer.R;
import com.example.myplayer.base.BasePager;
import com.example.myplayer.domain.MediaItem;
import com.example.myplayer.utils.TimeUtils;

import java.util.ArrayList;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/9
 * TODO:本地音乐页面
 */
public class AudioPager extends BasePager {

    private ListView lv_audio_pager;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    private ArrayList<MediaItem> mediaItems;            //视频实例容器
    private Handler handler;

    public AudioPager(Context context) {
        super(context);
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {           //切换到主线程
                super.handleMessage(msg);
                if (mediaItems != null && mediaItems.size() > 0) {       //有数据
                    tv_nomedia.setVisibility(View.GONE);
                    pb_loading.setVisibility(View.GONE);
                    //设置适配器
                    lv_audio_pager.setAdapter(new AudioPager.VideoPagerAdapter());
                } else {
                    tv_nomedia.setVisibility(View.VISIBLE);
                    pb_loading.setVisibility(View.GONE);
                }
            }
        };
    }


    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_audio_name;
        TextView tv_audio_duration;
        TextView tv_audio_size;
    }

    /**
     * TODO：返回每个音频的视图，点击音频播放
     * @return view：根据有无音频返回对应的页面
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.audio_pager, null);
        lv_audio_pager = view.findViewById(R.id.lv_audio_pager);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);
        //点击播放
        lv_audio_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaItem mediaItem = mediaItems.get(position);
                //调用其他播放器
//                Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//                context.startActivity(intent);
                //调用自己的播放器
                Intent intent = new Intent(context, MyVideoPlayer.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        Log.i("video pager", "initData: 本地音频初始化···");
        getData();                                           //尽量不用主线程
    }


    /**
     * TODO：适配器
     * return: 适配器
     */
    class VideoPagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_videopager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_video_icon);
                viewHolder.tv_audio_name = convertView.findViewById(R.id.tv_video_name);
                viewHolder.tv_audio_duration = convertView.findViewById(R.id.tv_video_duration);
                viewHolder.tv_audio_size = convertView.findViewById(R.id.tv_video_size);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            MediaItem mediaItem = mediaItems.get(position);
            viewHolder.tv_audio_name.setText(mediaItem.getName());
            viewHolder.tv_audio_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
            viewHolder.tv_audio_duration.setText(new TimeUtils().msToHMS(mediaItem.getDuration()));
            viewHolder.iv_icon.setImageResource(R.drawable.audiopager_audio);

            return convertView;
        }
    }


    /**
     * TODO:子线程加载本地音频信息
     */
    private void getData() {
        new Thread() {
            public void run() {
                mediaItems = new ArrayList<>();                 //容器
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;              //外部存储
                String[] objects = new String[]{
                        MediaStore.Audio.Media.DISPLAY_NAME,    //视频名称
                        MediaStore.Audio.Media.DURATION,        //时长
                        MediaStore.Audio.Media.SIZE,            //大小
                        MediaStore.Audio.Media.DATA             //绝对路径
                };
                //权限检查
                Cursor cursor = contentResolver.query(uri, objects, null, null, null);
                if (null != cursor) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(0);
                        long duration = cursor.getLong(1);
                        long size = cursor.getLong(2);
                        String data = cursor.getString(3);
                        Log.i("cursor ", "info: " + name+"\n"+duration+"\n"+size+"\n"+data+"\n");
                        MediaItem mediaItem = new MediaItem(name, duration, size, data);
                        mediaItems.add(mediaItem);           //添加到容器中
                    }
                    cursor.close();
                }
                handler.sendEmptyMessage(0);            //转到主线程
            }
        }.start();
    }
}
