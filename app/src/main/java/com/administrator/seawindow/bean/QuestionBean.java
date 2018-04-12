package com.administrator.seawindow.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */

public class QuestionBean{
    private int id;
    private String question;
    private String rightAnswer;
    private String chooseAnswer;
    private List<String> answers;

    public String getChooseAnswer() {
        return chooseAnswer;
    }

    public void setChooseAnswer(String chooseAnswer) {
        this.chooseAnswer = chooseAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

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
