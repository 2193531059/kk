package com.administrator.seawindow.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/9.
 */

public class KeyWordBean implements Serializable{
    private int id;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
