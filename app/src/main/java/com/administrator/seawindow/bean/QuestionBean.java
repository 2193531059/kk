package com.administrator.seawindow.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */

public class QuestionBean{
    private int id;
    private String question;
    private List<String> answers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswer() {
        return answers;
    }

    public void setAnswer(List<String> answer) {
        this.answers = answer;
    }
}
