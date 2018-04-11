package com.administrator.seawindow.bean;

import com.administrator.seawindow.utils.ConstantPool;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/8.
 */

public class SeaKnowledgeBean implements Serializable{
    private int id;
    private String cateName;
    private String imageUrl;
    private String text;

    public String getImageUrl() {
        return ConstantPool.HOST + imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
