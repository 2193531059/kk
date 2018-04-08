package com.administrator.seawindow.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/8.
 */

public class ActivityBean implements Serializable{
    private int id;
    private String activityTitle;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
