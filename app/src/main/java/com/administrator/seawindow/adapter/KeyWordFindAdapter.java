package com.administrator.seawindow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.KeyWordBean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */

public class KeyWordFindAdapter extends RecyclerView.Adapter{

    private List<KeyWordBean> mData;
    private LayoutInflater inflater;
    private Context context;
    private MyKeyWordClickListener clickListener;

    public KeyWordFindAdapter(Context context, List<KeyWordBean> mList) {
        inflater = LayoutInflater.from(context);
        mData = mList;
        this.context = context;
    }

    public void setClickListener(MyKeyWordClickListener listener){
        clickListener = listener;
    }

    public List<KeyWordBean> getmData(){
        return mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.key_word_find_item, parent, false);
        return new KeyWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        KeyWordBean bean = mData.get(position);
        String content = bean.getContent();
        ((KeyWordViewHolder)holder).content.setText(content);
        ((KeyWordViewHolder)holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickKeyWord(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface MyKeyWordClickListener{
        void onClickKeyWord(int position);
    }

    class KeyWordViewHolder extends RecyclerView.ViewHolder{

        private TextView content;
        private View view;

        public KeyWordViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            content = itemView.findViewById(R.id.content);
        }
    }
}
