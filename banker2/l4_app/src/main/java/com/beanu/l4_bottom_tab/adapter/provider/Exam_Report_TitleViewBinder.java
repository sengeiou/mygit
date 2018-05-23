package com.beanu.l4_bottom_tab.adapter.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 考题报告 试卷的模块标题
 * Created by Beanu on 2017/4/21.
 */
public class Exam_Report_TitleViewBinder extends ItemViewBinder<Exam_Report_Title, Exam_Report_TitleViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_exam_report_title, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Exam_Report_Title exam_Report_Title) {
        holder.mTxtExamTitle.setText(exam_Report_Title.getTitle());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_exam_title) TextView mTxtExamTitle;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
