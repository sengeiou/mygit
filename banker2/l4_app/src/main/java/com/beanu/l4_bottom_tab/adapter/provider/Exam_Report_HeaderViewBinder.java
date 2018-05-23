package com.beanu.l4_bottom_tab.adapter.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordJson;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 练习 报告 顶部信息
 * Created by Beanu on 2017/4/15.
 */
public class Exam_Report_HeaderViewBinder extends ItemViewBinder<AnswerRecordJson, Exam_Report_HeaderViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_exam_report_header, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AnswerRecordJson item) {
        holder.mTxtTitle.setText("练习类型：" + item.getCourseName());
        holder.mTxtDate.setText("交卷时间：" + item.getSubmitTime());

        String type = "";
        switch (item.getType()) {
            case 0:
                type = "专项练习";
                break;
            case 1:
                type = "智能练习";
                break;
            case 2:
                type = "历年真题";
                break;
        }
        holder.mTxtType.setText("考试类型：" + type);
        holder.mTxtInfo.setText(String.format("共%d道题 答对%d道 用时%s", item.getAnswerTotal(), item.getAnswerRealy(), item.getAnswerTime()));
        holder.mTxtDifficult.setText(item.getDifficulty());
        holder.mTxtRate.setText(item.getZql());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_title) TextView mTxtTitle;
        @BindView(R.id.txt_date) TextView mTxtDate;
        @BindView(R.id.txt_type) TextView mTxtType;
        @BindView(R.id.txt_info) TextView mTxtInfo;
        @BindView(R.id.txt_difficult) TextView mTxtDifficult;
        @BindView(R.id.txt_rate) TextView mTxtRate;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
