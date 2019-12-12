package com.example.myplayer.domain;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/11
 * TODO:视频或音频类
 */
public class MediaItem {
    private String name;            //视频名称
    private long duration;          //视频时长
    private long size;              //视频大小
    private String data;            //视频日期

    public MediaItem() {
    }

    public MediaItem(String name, long duration, long size, String data) {
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.data = data;
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
}
