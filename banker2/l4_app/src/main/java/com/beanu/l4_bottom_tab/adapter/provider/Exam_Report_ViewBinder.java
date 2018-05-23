package com.beanu.l4_bottom_tab.adapter.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordDetailJson;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 练习 报告 每一题的情况
 * Created by Beanu on 2017/4/15.
 */
public class Exam_Report_ViewBinder extends ItemViewBinder<AnswerRecordDetailJson, Exam_Report_ViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_exam_result, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AnswerRecordDetailJson item) {
        if (item.getqNo() != 0) {
            holder.mTxtNum.setText(item.getqNo() + "");
        } else {
            holder.mTxtNum.setText((getPosition(holder)) + "");
        }

        switch (item.getIsRealy()) {
            case 0:
                holder.mTxtNum.setBackgroundResource(R.drawable.practice_report_red);
                holder.mTxtNum.setSelected(true);
                break;
            case 1:
                holder.mTxtNum.setBackgroundResource(R.drawable.practice_report_green);
                holder.mTxtNum.setSelected(true);
                break;
            case 2:
                holder.mTxtNum.setBackgroundResource(R.drawable.practice_report_gray);
                holder.mTxtNum.setSelected(false);
                break;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_num) TextView mTxtNum;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
