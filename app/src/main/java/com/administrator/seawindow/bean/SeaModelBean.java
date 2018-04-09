package com.administrator.seawindow.bean;

import com.administrator.seawindow.utils.ConstantPool;

import java.io.Serializable;

/**
 * Created by Hero on 2018/4/6.
 */

public class SeaModelBean implements Serializable {
    private int id;
    private String title;
    private String video;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideo() {
        return ConstantPool.HOST + video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "SeaHotSpotBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
