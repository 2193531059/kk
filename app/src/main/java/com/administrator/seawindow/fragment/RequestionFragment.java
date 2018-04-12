package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.administrator.seawindow.bean.QuestionBean;
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
    private List<QuestionBean> mRightList;

    private Button startQuestion;
    private Button end_check_requestion;
    private Button research;
    private LinearLayout question_part;
    private LinearLayout loading_alert;
    private LinearLayout bottomPart;
    private RadioGroup answer_part;
    private Button pre_question, next_question, end_question;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
    private TextView question;
    private AlertDialog alertDialog;
    private LinearLayout right_answer_part;
    private TextView right_answer;

    private int questionCount;
    private int currentPosition;
    private int right;
    private int wrong;
    private boolean isCheckWrong = false;

    private final int GET_QUESTION_SUCCESS = 0;
    private final int GET_QUESTION_FAILED = 1;
    private final int ANSWER_RIGHT = 3;
    private final int ANSWER_WRONG = 4;

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
                case ANSWER_RIGHT:
                    QuestionBean beanRight = mList.get(currentPosition);
                    right++;
                    mRightList.add(beanRight);
                    break;
                case ANSWER_WRONG:
                    QuestionBean beanWrong = mList.get(currentPosition);
                    wrong++;
                    mWrongList.add(beanWrong);
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

        research = view.findViewById(R.id.research);
        research.setOnClickListener(this);

        right_answer_part = view.findViewById(R.id.right_answer_part);
        right_answer_part.setVisibility(View.GONE);
        right_answer = view.findViewById(R.id.right_answer);

        bottomPart = view.findViewById(R.id.bottom_part);
        bottomPart.setVisibility(View.GONE);
        end_check_requestion = view.findViewById(R.id.end_check_requestion);
        end_check_requestion.setOnClickListener(this);

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
                if (currentPosition > 0 || currentPosition < mList.size()) {
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
                                chooseAnser = "E";
                            }
                            break;
                    }
                    if (!TextUtils.isEmpty(chooseAnser)) {
                        bean.setChooseAnswer(chooseAnser);
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
        mRightList = new ArrayList<>();

//        QuestionBean bean1 = new QuestionBean();
//        bean1.setQuestion("海洋中最大的动物是什么？");
//        List<String> answers1 = new ArrayList<>();
//        answers1.add("鲨鱼");
//        answers1.add("海象");
//        answers1.add("蓝鲸");
//        bean1.setAnswer(answers1);
//
//        QuestionBean bean2 = new QuestionBean();
//        bean2.setQuestion("海龟生蛋是在什么地方？");
//        List<String> answers2 = new ArrayList<>();
//        answers2.add("海水里");
//        answers2.add("沙滩上");
//        answers2.add("海草");
//        bean2.setAnswer(answers2);
//
//        QuestionBean bean3 = new QuestionBean();
//        bean3.setQuestion("珊瑚是？");
//        List<String> answers3 = new ArrayList<>();
//        answers3.add("动物");
//        answers3.add("植物");
//        answers3.add("都不是");
//        bean3.setAnswer(answers3);
//
//        mList.add(bean1);
//        mList.add(bean2);
//        mList.add(bean3);

        getQuestionData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_question:
                if (mList.size() == 0) {
                    ToastUtil.show(getActivity(), R.string.no_question);
                    research.setVisibility(View.VISIBLE);
                    startQuestion.setVisibility(View.GONE);
                    return;
                }
                research.setVisibility(View.GONE);
                question_part.setVisibility(View.VISIBLE);
                bottomPart.setVisibility(View.VISIBLE);
                pre_question.setVisibility(View.GONE);
                startQuestion.setVisibility(View.GONE);
                questionCount = mList.size();
                currentPosition = 0;
                if (isCheckWrong) {
                    right_answer_part.setVisibility(View.VISIBLE);
                    end_check_requestion.setVisibility(View.VISIBLE);
                    end_question.setVisibility(View.GONE);
                    Log.e(TAG, "onClick: currentPosition = " + currentPosition);
                    Log.e(TAG, "onClick: mList = " + mList.size());
                    if (currentPosition == questionCount - 1) {
                        end_check_requestion.setVisibility(View.VISIBLE);
                    } else {
                        end_check_requestion.setVisibility(View.VISIBLE);
                        next_question.setVisibility(View.VISIBLE);
                        pre_question.setVisibility(View.GONE);
                    }
                } else {
                    right_answer_part.setVisibility(View.GONE);
                    end_check_requestion.setVisibility(View.GONE);
                    if (currentPosition == questionCount - 1) {
                        end_question.setVisibility(View.VISIBLE);
                    } else {
                        next_question.setVisibility(View.VISIBLE);
                    }
                }
                setQuestionView();
                break;
            case R.id.pre_question:
                currentPosition--;
                if (currentPosition < 0) {
                    return;
                }
                if (isCheckWrong) {
                    right_answer_part.setVisibility(View.VISIBLE);
                    end_check_requestion.setVisibility(View.VISIBLE);
                    end_question.setVisibility(View.GONE);
                } else {
                    right_answer_part.setVisibility(View.GONE);
                    end_check_requestion.setVisibility(View.GONE);
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
                if (isCheckWrong) {
                    right_answer_part.setVisibility(View.VISIBLE);
                    end_check_requestion.setVisibility(View.VISIBLE);
                } else {
                    right_answer_part.setVisibility(View.GONE);
                    end_check_requestion.setVisibility(View.GONE);
                }
                if (currentPosition == (questionCount - 1)) {
                    pre_question.setVisibility(View.VISIBLE);
                    next_question.setVisibility(View.GONE);
                    if (isCheckWrong) {
                        end_question.setVisibility(View.GONE);
                    } else {
                        end_question.setVisibility(View.VISIBLE);
                    }
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
            case R.id.end_check_requestion:
                loading_alert.setVisibility(View.VISIBLE);
                bottomPart.setVisibility(View.GONE);
                startQuestion.setVisibility(View.GONE);
                question_part.setVisibility(View.GONE);
                right_answer.setVisibility(View.GONE);
                mList.clear();
                mWrongList.clear();
                mRightList.clear();
                right = 0;
                wrong = 0;
                isCheckWrong = false;
                getQuestionData();
                break;
            case R.id.research:
                loading_alert.setVisibility(View.VISIBLE);
                bottomPart.setVisibility(View.GONE);
                startQuestion.setVisibility(View.GONE);
                question_part.setVisibility(View.GONE);
                mList.clear();
                mWrongList.clear();
                mRightList.clear();
                right = 0;
                wrong = 0;
                isCheckWrong = false;
                getQuestionData();
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
        if (isCheckWrong) {
            String rightAnswer = bean.getRightAnswer();
            String chooseAnswer = bean.getChooseAnswer();
            right_answer.setVisibility(View.VISIBLE);
            right_answer.setText(getString(R.string.right_answer) + rightAnswer + getString(R.string.choose_user) + chooseAnswer);
        }
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
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        if (alertDialog.getWindow() == null) return;
        alertDialog.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
        TextView msg = alertDialog.findViewById(R.id.tv_msg);
        Button reanswer = alertDialog.findViewById(R.id.btn_reanswer);

        Button checkWrong = alertDialog.findViewById(R.id.btn_checkWrong);
        if (msg == null || reanswer == null || checkWrong == null) return;

        msg.setText(getString(R.string.question_message) + right + getString(R.string.item_1) + wrong + getString(R.string.item_2));

        reanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                loading_alert.setVisibility(View.VISIBLE);
                bottomPart.setVisibility(View.GONE);
                startQuestion.setVisibility(View.GONE);
                question_part.setVisibility(View.GONE);
                mList.clear();
                mWrongList.clear();
                mRightList.clear();
                right = 0;
                wrong = 0;
                isCheckWrong = false;
                getQuestionData();
            }
        });
        checkWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wrong == 0) {
                    ToastUtil.show(getActivity(), R.string.no_wrong);
                    alertDialog.dismiss();
                    loading_alert.setVisibility(View.VISIBLE);
                    bottomPart.setVisibility(View.GONE);
                    startQuestion.setVisibility(View.GONE);
                    question_part.setVisibility(View.GONE);
                    mList.clear();
                    mWrongList.clear();
                    mRightList.clear();
                    right = 0;
                    wrong = 0;
                    isCheckWrong = false;
                    getQuestionData();
                } else {
                    alertDialog.dismiss();
                    mList.clear();
                    mList.addAll(mWrongList);
                    mWrongList.clear();
                    mRightList.clear();
                    wrong = 0;
                    right = 0;
                    isCheckWrong = true;
                    startQuestion.performClick();
                }
            }
        });
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
                            bean.setRightAnswer(obj.optString("rightAnswers"));
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
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        String mes = obj.optString("mes");
                        if (mes.contains("错误")) {
                            Message msg = mHandler.obtainMessage(ANSWER_WRONG);
                            mHandler.sendMessage(msg);
                        } else if (mes.contains("正确")) {
                            Message msg = mHandler.obtainMessage(ANSWER_RIGHT);
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    private void getQuestionData(){
        getQuestion();
    }
}
