package com.administrator.seawindow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.seawindow.R;
import com.administrator.seawindow.adapter.RequestionAdapter;
import com.administrator.seawindow.bean.QuestionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class RequestionFragment extends Fragment {
    private RecyclerView recycler_question;
    private RequestionAdapter mAdapter;
    private List<QuestionBean> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requestion_layout, null);
        initData();
        initView(view);
        return view;
    }

    private void initView(View view){
        recycler_question = view.findViewById(R.id.recycler_question);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_question.setLayoutManager(linearLayoutManager);
        recycler_question.setHasFixedSize(true);
        mAdapter = new RequestionAdapter(mList, getActivity());
        recycler_question.setAdapter(mAdapter);
    }

    private void initData(){
        mList = new ArrayList<>();
        QuestionBean bean1 = new QuestionBean();
        bean1.setQuestion("问：讲讲世界上最大的细菌");
        bean1.setAnswer("答：世界上最大的细菌（们）几乎全都是海洋里发现的，而且它们大多都是硫化细菌。" +
                "目前发现的最大的细菌叫Thiomargarita namibiensis. 这个名字的前半截Thio是指和硫相关的，" +
                "margarita可不是大家熟悉的那种蘸着盐粒的酒，在拉丁语中margarita指『闪耀着柔和光泽的珍珠项链，" +
                "而这个名字的后半截是指纳米比亚，这种细菌第一次被发现的地方。纳米比亚的海边淤泥富含有机物，" +
                "这些有机物被厌氧生物降解后生成很多硫化氢。我们知道硫化氢对人和动物是有毒的，但是硫化细菌可喜欢硫化氢了，" +
                "因为它们可以通过氧化硫化氢获得能量，这也是它们被叫做硫化细菌的原因。我们的主角Thiomargarita namibiensis也是其中一员。");
        QuestionBean bean2 = new QuestionBean();
        bean2.setQuestion("问：电影《后天》（海洋洋流变化导致气候灾难的灾难科幻电影）是否有一定的科学依据？");
        bean2.setAnswer("答：首先得了解海洋在气候变化中担当着什么样的角色。海洋承载了全球90%的热量，" +
                "而水是自然界中比热容最大的物质，海洋能够吸收大量热量而不引起很大的温度变化，所以海洋充当着中央空调的作用，" +
                "能制冷也能制暖。而海洋对气候最重要的作用，就是通过洋流调节气候。整个地球行星尺度的洋流循环，是“温盐环流”，也就是电影《后天》" +
                "气候变异的直接因素。");
        QuestionBean bean3 = new QuestionBean();
        bean3.setQuestion("问：海洋有哪些有趣的动物?");
        bean3.setAnswer("答：当海参遇到敌害进攻无法脱身时，可以用分身法逃命，内脏迅速从肛门抛出。" +
                "天敌看到颜色鲜艳的美味，就会舍本逐末地扑向海参的内脏。弃内脏的海参还可以活着。" +
                "乌贼被称为海中\"化妆师\"，因为它实在太爱\"打扮\"了。乌贼十分善于利用体色表达感情。" +
                "它体色发生突变，多半是因为感到恐惧和激动。到繁殖季节，雌乌贼用五彩缤纷的颜色表达对异性的爱慕。" +
                "它们常常在自己的躯干上涂上一道道斑纹，犹如穿上了漂亮的睡衣。在海洋生物中, 乌贼的游泳速度最快。" +
                "它是靠肚皮上的漏斗管喷水的反作用力飞速前进，能跳出水面高达7米到10米。乌贼肚子里藏有墨汁，" +
                "这在动物界是罕见的。墨囊在乌贼的肚子里，囊内有墨腺，能分泌浓厚的墨汁，而且喷出后能迅速补充。" +
                "当乌贼和强敌突然相遇时，它就利用这种特殊的防御武器使自己转危为安。  ");
        mList.add(bean1);
        mList.add(bean2);
        mList.add(bean3);
    }
}
