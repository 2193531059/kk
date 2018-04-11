package com.administrator.seawindow.bean;

import com.administrator.seawindow.utils.ConstantPool;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hero on 2018/4/6.
 */

public class SeaHotSpotBean implements Serializable {
    private int id;//热点ID
    private String title;//热点标题
    private String image;//热点缩略图
    private String text;//热点文本
    private String source;//热点来源
    private String publishTime;//热点发表时间
    private List<CommentBean> comments;//评论

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }

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

    public String getImage() {
        return ConstantPool.HOST  + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "SeaHotSpotBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", comments=" + comments +
                '}';
    }
}
