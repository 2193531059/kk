package com.administrator.seawindow.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/8.
 */

public class VideoBean implements Serializable{
    private int id;
    private String video;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
