package com.administrator.seawindow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.QuestionBean;
import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */

public class RequestionAdapter extends RecyclerView.Adapter{

    private List<QuestionBean> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public RequestionAdapter(List<QuestionBean> mData, Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_item_question,parent,false);
        return new RequestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuestionBean bean = mData.get(position);
        String question = bean.getQuestion();
        String answer = bean.getAnswer();
        ((RequestionViewHolder)holder).question.setText(question);
        ((RequestionViewHolder)holder).answer.setText(answer);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class RequestionViewHolder extends RecyclerView.ViewHolder{

        private TextView question, answer;

        public RequestionViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
        }
    }
}
