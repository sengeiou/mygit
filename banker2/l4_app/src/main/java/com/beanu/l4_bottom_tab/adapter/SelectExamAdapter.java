package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.Subject;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择考试分类
 * Created by Beanu on 2017/3/6.
 */

public class SelectExamAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Subject> groupList;
    private int[] selectedItem;//被选中的

    public SelectExamAdapter(Context context, List<Subject> groupList) {
        mContext = context;
        this.groupList = groupList;
        selectedItem = new int[]{-1, -1};
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupList.get(i).getSecSubList() == null ? 0 : groupList.get(i).getSecSubList().size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return groupList.get(i).getSecSubList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        GroupHolder groupHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.expend_list_exam_group, null);
            groupHolder = new GroupHolder(view);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }

        Subject examItem = groupList.get(groupPosition);

        if (examItem.getSecSubList() != null && examItem.getSecSubList().size() > 0) {
            if (!isExpanded) {
                groupHolder.arrow.setImageResource(R.drawable.pay_select_x);
            } else {
                groupHolder.arrow.setImageResource(R.drawable.pay_select_s);
            }
        } else {
            groupHolder.arrow.setImageResource(0);
        }

        groupHolder.txt.setText(examItem.getName());
        if (!TextUtils.isEmpty(examItem.getIcoUrl())) {
            Glide.with(mContext).load(examItem.getIcoUrl()).into(groupHolder.img);
        }

        if (examItem.getId().equals(AppHolder.getInstance().user.getSubjectId())) {
            groupHolder.mRadioButton.setChecked(true);
        } else {
            groupHolder.mRadioButton.setChecked(false);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expend_list_exam_item, null);
            itemHolder = new ItemHolder(convertView);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }

        itemHolder.groupPosition = groupPosition;  //在viewholder中纪录当前的groupPosition
        itemHolder.childPosition = childPosition;

        if (selectedItem[0] == groupPosition && selectedItem[1] == childPosition) {  //判断当前条目是否被选中
            itemHolder.mRadioButton.setChecked(true);
        } else {
            itemHolder.mRadioButton.setChecked(false);
        }

        Subject subject = ((Subject) getChild(groupPosition, childPosition));
        if (subject.getId().equals(AppHolder.getInstance().user.getSubjectId())) {
            itemHolder.mRadioButton.setChecked(true);
        } else {
            itemHolder.mRadioButton.setChecked(false);
        }

        itemHolder.txt.setText(subject.getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    class GroupHolder {

        @BindView(R.id.item_exam_img) ImageView img;
        @BindView(R.id.item_exam_name) TextView txt;
        @BindView(R.id.item_exam_arrow) ImageView arrow;
        @BindView(R.id.item_exam_tag) RadioButton mRadioButton;

        public GroupHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ItemHolder {

        int groupPosition;
        int childPosition;
        @BindView(R.id.item_name) TextView txt;
        @BindView(R.id.item_radio) RadioButton mRadioButton;
        @BindView(R.id.rl_item) RelativeLayout mLayout;

        public ItemHolder(View view) {
            ButterKnife.bind(this, view);

            mLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        selectedItem = new int[]{groupPosition, childPosition};
                        KLog.d(groupPosition + "==" + childPosition);
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });
        }
    }
}
