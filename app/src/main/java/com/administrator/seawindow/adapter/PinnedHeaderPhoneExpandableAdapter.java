package com.administrator.seawindow.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.SeaKnowledgeBean;
import com.administrator.seawindow.view.PinnedHeaderExpandableListView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/7/10.
 */
public class PinnedHeaderPhoneExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter {
    private PinnedHeaderExpandableListView listView;
    private ArrayList<String> group;
    private ArrayList<ArrayList<SeaKnowledgeBean>> child;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;
    private Picasso mPicasso;

    public PinnedHeaderPhoneExpandableAdapter(Context context, ArrayList<String> group, ArrayList<ArrayList<SeaKnowledgeBean>> child, PinnedHeaderExpandableListView listView) {
        mContext = context;
        mPicasso = Picasso.with(context);
        this.group = group;
        this.child = child;
        this.listView = listView;
        mInflater = LayoutInflater.from(mContext);
    }

    public void addChild(ArrayList<SeaKnowledgeBean> list) {
        child.add(list);

        notifyDataSetChanged();

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    public ArrayList<ArrayList<SeaKnowledgeBean>> getData(){
        if(child == null){
            child = new ArrayList<>();
        }
        return child;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ArrayList<SeaKnowledgeBean> list = child.get(groupPosition);
        if (childPosition >= list.size()) {
            return null;
        }
        SeaKnowledgeBean bean = list.get(childPosition);

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.knowledge_item_child, parent, false);
            holder.child_part = convertView.findViewById(R.id.child_part);
            holder.iv_knowLedge = convertView.findViewById(R.id.iv_knowLedge);
            holder.tv_knowLedge = convertView.findViewById(R.id.tv_knowLedge);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mListener != null) {
            holder.child_part.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClick(groupPosition, childPosition);
                }
            });
        }
        String imgUrl = bean.getImageUrl();
        String title = bean.getCateName();
        mPicasso.load(imgUrl).into(holder.iv_knowLedge);
        holder.tv_knowLedge.setText(title);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (child == null) {
            return 0;
        }
        if (groupPosition >= child.size()) {
            return 0;
        }
        if (child.size() != 0) {
            return child.get(groupPosition).size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createGroupView();
        }

        ImageView iv = view.findViewById(R.id.group_icon);
        if (isExpanded) {
            iv.setImageResource(R.drawable.zhankai);
        } else {
            iv.setImageResource(R.drawable.shouqi);
        }

        TextView text = view.findViewById(R.id.group_title);
        ImageView iv1 = view.findViewById(R.id.iv1);
        String title = group.get(groupPosition);
        text.setText(title);

        switch (title){
            case "海洋奥秘":
                iv1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sea_secret));
                break;
            case "海洋生态":
                iv1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sea_zoology));
                break;
            case "海洋资源":
                iv1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sea_resouce));
                break;
            case "海洋国防":
                iv1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sea_defence));
                break;
            case "远洋航海":
                iv1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ocean_sailing));
                break;
            case "滨海旅游":
                iv1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.coastal_tourism));
                break;
        }
        return view;

    }

    private View createGroupView() {
        return mInflater.inflate(R.layout.knowledge_group, null);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1 && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void configureHeader(View header, int groupPosition,
                                int childPosition, int alpha) {
        String groupData = group.get(groupPosition);
        ((TextView) header.findViewById(R.id.group_title)).setText(groupData);
        switch (groupData){
            case "海洋奥秘":
                (header.findViewById(R.id.iv1)).setBackground(ContextCompat.getDrawable(mContext, R.drawable.sea_secret));
                break;
            case "海洋生态":
                (header.findViewById(R.id.iv1)).setBackground(ContextCompat.getDrawable(mContext, R.drawable.sea_zoology));
                break;
            case "海洋资源":
                (header.findViewById(R.id.iv1)).setBackground(ContextCompat.getDrawable(mContext, R.drawable.sea_resouce));
                break;
            case "海洋国防":
                (header.findViewById(R.id.iv1)).setBackground(ContextCompat.getDrawable(mContext, R.drawable.sea_defence));
                break;
            case "远洋航海":
                (header.findViewById(R.id.iv1)).setBackground(ContextCompat.getDrawable(mContext, R.drawable.ocean_sailing));
                break;
            case "滨海旅游":
                (header.findViewById(R.id.iv1)).setBackground(ContextCompat.getDrawable(mContext, R.drawable.coastal_tourism));
                break;
        }
    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

    @Override
    public void setGroupClickStatus(int groupPosition, int status) {
        if (mListener != null) {
            mListener.onGroupExpand(groupPosition);
        }
        groupStatusMap.put(groupPosition, status);
    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.keyAt(groupPosition) >= 0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int groupPosition, int childPosition);
        void onGroupExpand(int groupPosition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder {
        private LinearLayout child_part;
        private ImageView iv_knowLedge;
        private TextView tv_knowLedge;
    }
}
