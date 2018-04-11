package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.CommentBean;
import com.administrator.seawindow.bean.QuestionBean;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

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

    private final int GET_QUESTION_SUCCESS = 0;
    private final int GET_QUESTION_FAILED = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_QUESTION_SUCCESS:
                    loading_alert.setVisibility(View.GONE);
                    startQuestion.setVisibility(View.VISIBLE);
                    break;
                case GET_QUESTION_FAILED:
                    loading_alert.setVisibility(View.GONE);
                    startQuestion.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

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
                QuestionBean bean = null;
                if (currentPosition < 0 || currentPosition >= mList.size()) {
                    bean = mList.get(currentPosition);
                }
                if (bean != null) {
                    int id = bean.getId();
                    String chooseAnser = null;
                    switch (checkedId){
                        case R.id.radio_1:
                            if (radioButton1.isChecked()) {
                                chooseAnser = "A";
                            }
                            break;
                        case R.id.radio_2:
                            if (radioButton2.isChecked()) {
                                chooseAnser = "B";
                            }
                            break;
                        case R.id.radio_3:
                            if (radioButton3.isChecked()) {
                                chooseAnser = "C";
                            }
                            break;
                        case R.id.radio_4:
                            if (radioButton4.isChecked()) {
                                chooseAnser = "D";
                            }
                            break;
                        case R.id.radio_5:
                            if (radioButton5.isChecked()) {
                                chooseAnser = "F";
                            }
                            break;
                    }
                    if (!TextUtils.isEmpty(chooseAnser)) {
                        checkQuestion(id, chooseAnser);
                    }
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

    @SuppressLint("StaticFieldLeak")
    private void getQuestion() {
        new AsyncTask<Void, Void, Void>() {
            String json = null;

            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_QUESTION);
                try {
                    if (response != null) {
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:getQuestion json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:getQuestion e = " + e);
                    Message msg = mHandler.obtainMessage(GET_QUESTION_FAILED);
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONArray array = new JSONArray(json);
                        for (int i = 0;i<array.length();i++) {
                            JSONObject obj = array.getJSONObject(i);
                            QuestionBean bean = new QuestionBean();
                            List<String> answers = new ArrayList<>();
                            answers.add(obj.optString("answersA"));
                            answers.add(obj.optString("answersB"));
                            answers.add(obj.optString("answersC"));
                            bean.setAnswer(answers);
                            bean.setId(obj.optInt("id"));
                            bean.setQuestion(obj.optString("questions"));
                            mList.add(bean);
                        }
                        Message msg = mHandler.obtainMessage(GET_QUESTION_SUCCESS);
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Message msg = mHandler.obtainMessage(GET_QUESTION_FAILED);
                        mHandler.sendMessage(msg);
                    }
                } else {
                    Message msg = mHandler.obtainMessage(GET_QUESTION_FAILED);
                    mHandler.sendMessage(msg);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void checkQuestion(final int id, final String item) {
        new AsyncTask<Void, Void, Void>() {
            String json = null;

            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.CHECNK_ANSWER + "?id=" + id + "&chooseAnswers=" + item);
                try {
                    if (response != null) {
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:getQuestion json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:getQuestion e = " + e);
                    Message msg = mHandler.obtainMessage(GET_QUESTION_FAILED);
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {

                } else {

                }
            }
        }.execute();
    }

    private void getQuestionData(){
        getQuestion();
    }
}
