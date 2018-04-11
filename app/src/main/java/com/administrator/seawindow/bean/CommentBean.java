package com.administrator.seawindow.bean;

import com.administrator.seawindow.utils.ConstantPool;

import java.io.Serializable;

/**
 * Created by Hero on 2018/4/11.
 */

public class CommentBean implements Serializable{
    private String image;
    private int newsID;
    private String nickName;
    private String text;

    public String getImage() {
        return ConstantPool.HOST + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNewsID() {
        return newsID;
    }

    public void setNewsID(int newsID) {
        this.newsID = newsID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
