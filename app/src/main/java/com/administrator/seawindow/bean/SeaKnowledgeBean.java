package com.administrator.seawindow.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/8.
 */

public class SeaKnowledgeBean implements Serializable{
    private int id;
    private String cateName;
    private String text;

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
