package com.example.myplayer.domain;

import android.net.Uri;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/11
 * TODO:视频或音频类
 */
public class MediaItem {
    private String name;            //视频名称
    private long duration;          //视频时长
    private long size;              //视频大小
    private String data;            //视频地址
    private String coverImg;        //封面地址
    private String videoTitle;      //视频标题
    private String artistName;      //演唱者
    private Uri netUri;

    public MediaItem() {
    }

    //网络视频构造器
    public MediaItem(String name, Uri uri, String coverImg, String videoTitle) {
        this.name = name;
        this.netUri = uri;
        this.coverImg = coverImg;
        this.videoTitle = videoTitle;
    }
    //本地视频构造器
    public MediaItem(String name, long duration, long size, String data) {
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.data = data;
    }
    //本地音乐构造器
    public MediaItem(String name, long duration, long size, String data,String artistName) {
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.data = data;
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Uri getNetUri() {
        return netUri;
    }

    public void setNetUri(Uri netUri) {
        this.netUri = netUri;
    }
}
