package com.administrator.seawindow.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.QuestionBean;
import com.administrator.seawindow.utils.DialogUtil;
import com.administrator.seawindow.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class RequestionFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "RequestionFragment";
    private List<QuestionBean> mList;
    private List<QuestionBean> mWrongList;

    private Button startQuestion;
    private LinearLayout question_part;
    private LinearLayout loading_alert;
    private LinearLayout bottomPart;
    private RadioGroup answer_part;
    private Button pre_question, next_question, end_question;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
    private TextView question;

    private int questionCount;
    private int currentPosition;
    private int right;
    private int wrong;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requestion_layout, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        loading_alert = view.findViewById(R.id.loading_alert);
        loading_alert.setVisibility(View.VISIBLE);

        bottomPart = view.findViewById(R.id.bottom_part);
        bottomPart.setVisibility(View.GONE);

        startQuestion = view.findViewById(R.id.start_question);
        startQuestion.setVisibility(View.GONE);
        startQuestion.setOnClickListener(this);

        question_part = view.findViewById(R.id.question_part);
        question_part.setVisibility(View.GONE);
        answer_part = view.findViewById(R.id.answer_part);
        answer_part.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                QuestionBean bean = mList.get(currentPosition);
                int id = bean.getId();
                List<String> answers = bean.getAnswer();
                String answer = null;
                switch (checkedId){
                    case R.id.radio_1:
                        if (radioButton1.isChecked()) {
                            answer = answers.get(0);
                        }
                        break;
                    case R.id.radio_2:
                        if (radioButton2.isChecked()) {
                            answer = answers.get(1);
                        }
                        break;
                    case R.id.radio_3:
                        if (radioButton3.isChecked()) {
                            answer = answers.get(2);
                        }
                        break;
                    case R.id.radio_4:
                        if (radioButton4.isChecked()) {
                            answer = answers.get(3);
                        }
                        break;
                    case R.id.radio_5:
                        if (radioButton5.isChecked()) {
                            answer = answers.get(4);
                        }
                        break;
                }
                if (!TextUtils.isEmpty(answer)) {
                    ToastUtil.show(getActivity(), answer);
                }
            }
        });

        radioButton1 = view.findViewById(R.id.radio_1);
        radioButton2 = view.findViewById(R.id.radio_2);
        radioButton3 = view.findViewById(R.id.radio_3);
        radioButton4 = view.findViewById(R.id.radio_4);
        radioButton5 = view.findViewById(R.id.radio_5);

        pre_question = view.findViewById(R.id.pre_question);
        next_question = view.findViewById(R.id.next_question);
        end_question = view.findViewById(R.id.end_answer);
        pre_question.setOnClickListener(this);
        next_question.setOnClickListener(this);
        end_question.setOnClickListener(this);

        question = view.findViewById(R.id.question);
    }

    private void initData(){
        mList = new ArrayList<>();
        mWrongList = new ArrayList<>();
        QuestionBean bean1 = new QuestionBean();
        bean1.setQuestion("海洋中最大的动物是什么？");
        List<String> answers1 = new ArrayList<>();
        answers1.add("鲨鱼");
        answers1.add("海象");
        answers1.add("蓝鲸");
        bean1.setAnswer(answers1);

        QuestionBean bean2 = new QuestionBean();
        bean2.setQuestion("海龟生蛋是在什么地方？");
        List<String> answers2 = new ArrayList<>();
        answers2.add("海水里");
        answers2.add("沙滩上");
        answers2.add("海草");
        bean2.setAnswer(answers2);

        QuestionBean bean3 = new QuestionBean();
        bean3.setQuestion("珊瑚是？");
        List<String> answers3 = new ArrayList<>();
        answers3.add("动物");
        answers3.add("植物");
        answers3.add("都不是");
        bean3.setAnswer(answers3);

        mList.add(bean1);
        mList.add(bean2);
        mList.add(bean3);

        getQuestionData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_question:
                question_part.setVisibility(View.VISIBLE);
                bottomPart.setVisibility(View.VISIBLE);
                pre_question.setVisibility(View.GONE);
                startQuestion.setVisibility(View.GONE);
                questionCount = mList.size();
                currentPosition = 0;
                setQuestionView();
                break;
            case R.id.pre_question:
                currentPosition--;
                if (currentPosition < 0) {
                    return;
                }
                if (currentPosition == 0) {
                    pre_question.setVisibility(View.GONE);
                    next_question.setVisibility(View.VISIBLE);
                    end_question.setVisibility(View.GONE);
                } else {
                    pre_question.setVisibility(View.VISIBLE);
                    next_question.setVisibility(View.VISIBLE);
                    end_question.setVisibility(View.GONE);
                }
                setQuestionView();
                break;
            case R.id.next_question:
                currentPosition++;
                if (currentPosition >= questionCount) {
                    return;
                }
                if (currentPosition == (questionCount - 1)) {
                    pre_question.setVisibility(View.VISIBLE);
                    next_question.setVisibility(View.GONE);
                    end_question.setVisibility(View.VISIBLE);
                } else {
                    pre_question.setVisibility(View.VISIBLE);
                    next_question.setVisibility(View.VISIBLE);
                    end_question.setVisibility(View.GONE);
                }
                setQuestionView();
                break;
            case R.id.end_answer:
                showResultView();
                break;
        }
    }

    private void setQuestionView(){
        answer_part.clearCheck();
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);
        radioButton4.setChecked(false);
        radioButton5.setChecked(false);

        QuestionBean bean = mList.get(currentPosition);
        question.setText(bean.getQuestion());
        List<String> answers = bean.getAnswer();
        int size = answers.size();
        switch (size) {
            case 1:
                radioButton1.setText(answers.get(0));
                radioButton2.setVisibility(View.GONE);
                radioButton3.setVisibility(View.GONE);
                radioButton4.setVisibility(View.GONE);
                radioButton5.setVisibility(View.GONE);
                break;
            case 2:
                radioButton1.setText(answers.get(0));
                radioButton2.setText(answers.get(1));
                radioButton3.setVisibility(View.GONE);
                radioButton4.setVisibility(View.GONE);
                radioButton5.setVisibility(View.GONE);
                break;
            case 3:
                radioButton1.setText(answers.get(0));
                radioButton2.setText(answers.get(1));
                radioButton3.setText(answers.get(2));
                radioButton4.setVisibility(View.GONE);
                radioButton5.setVisibility(View.GONE);
                break;
            case 4:
                radioButton1.setText(answers.get(0));
                radioButton2.setText(answers.get(1));
                radioButton3.setText(answers.get(2));
                radioButton4.setText(answers.get(3));
                radioButton5.setVisibility(View.GONE);
                break;
            case 5:
                radioButton1.setText(answers.get(0));
                radioButton2.setText(answers.get(1));
                radioButton3.setText(answers.get(2));
                radioButton4.setText(answers.get(3));
                radioButton5.setText(answers.get(5));
                break;
        }
    }

    private void showResultView(){

    }

    private void getQuestionData(){
        loading_alert.setVisibility(View.GONE);
        startQuestion.setVisibility(View.VISIBLE);
    }
}
