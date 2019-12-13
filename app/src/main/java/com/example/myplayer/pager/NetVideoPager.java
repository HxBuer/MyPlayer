package com.example.myplayer.pager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myplayer.R;
import com.example.myplayer.base.BasePager;
import com.example.myplayer.domain.MediaItem;
import com.example.myplayer.utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/9
 * TODO:网络视频页面
 */
public class NetVideoPager extends BasePager {

    private ListView lv_video_pager;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    private ArrayList<MediaItem> mediaItems;
    private MyNetVideoAdapter myAdaper;

    public NetVideoPager(Context context){
        super(context);
        mediaItems = new ArrayList<>();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.net_video_pager,null);
        lv_video_pager = view.findViewById(R.id.lv_video_pager);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);
        return view;
    }

    @Override
    public void initData() {
        Log.i("", "initData: 网络数据初始化···");
        getDataFromNet();
    }

    /**
     * TODO:联网请求数据
     */
    private void getDataFromNet(){
        RequestParams params = new RequestParams(URL.NET_VIDEO_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("联网请求", "onSuccess: OK！" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("联网请求", "onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i("联网请求", "onCancelled");
            }

            @Override
            public void onFinished() {
                Log.i("联网请求", "onFinished");
            }
        });
    }


    /**
     * TODO:调用解析函数，并设置适配器
     * @param json
     */
    private void processData(String json) {
        parseJson(json);
        pb_loading.setVisibility(View.GONE);
        if(mediaItems != null && mediaItems.size() > 0){
            tv_nomedia.setVisibility(View.GONE);
        }else {
            tv_nomedia.setVisibility(View.VISIBLE);
        }
        //设置适配器
        myAdaper = new MyNetVideoAdapter();
        lv_video_pager.setAdapter(myAdaper);
    }

    class MyNetVideoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mediaItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = View.inflate(context , R.layout.item_netvido_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_movie_icon = convertView.findViewById(R.id.iv_movie_icon);
                viewHolder.tv_video_title = convertView.findViewById(R.id.tv_video_title);
                viewHolder.tv_movie_name  = convertView.findViewById(R.id.tv_movie_name);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //根据位置得到对应的数据
            MediaItem mediaItem = mediaItems.get(position);
            viewHolder.tv_video_title.setText(mediaItem.getVideoTitle());
            viewHolder.tv_movie_name.setText(mediaItem.getName());

            //请求图片：XUtils3或者Glide
            x.image().bind(viewHolder.iv_movie_icon , mediaItem.getCoverImg());
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_movie_icon;
        TextView tv_video_title;
        TextView tv_movie_name;

    }


    /**
     * TODO:解析json数据
     * @param json
     * 解析方法：1.手动解析（系统的接口）
     *         2.用第三方解析工具：gson和fastjson
     */
    private void parseJson(String json){
    try {
        JSONObject objects = new JSONObject(json);
        JSONArray jsonArray = objects.optJSONArray("trailers");     //此处最好不用getJSONArray,若trailers不存在会崩溃
        if (jsonArray != null) {
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (null != jsonObject){
                    String url  = jsonObject.getString("url");
                    String movieName = jsonObject.getString("movieName");
                    String coverImg = jsonObject.getString("coverImg");
                    String videoTitle = jsonObject.getString("videoTitle");
                    MediaItem mediaItem = new MediaItem(movieName,url,coverImg,videoTitle);
                    mediaItems.add(mediaItem);
                }
            }
        }
    } catch (JSONException e) {
        Log.i("", "parseJson: Exception");
        e.printStackTrace();
    }
    }
}
