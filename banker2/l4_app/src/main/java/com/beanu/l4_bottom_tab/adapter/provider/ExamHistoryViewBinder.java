package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.ExamHistory;
import com.beanu.l4_bottom_tab.ui.module1_exam.ExamActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 历年真题
 * Created by Beanu on 2017/3/29.
 */
public class ExamHistoryViewBinder extends ItemViewBinder<ExamHistory, ExamHistoryViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_exam_history, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ExamHistory examHistory) {

        final Context context = holder.itemView.getContext();

        holder.mTxtExamTitle.setText(examHistory.getName());
        holder.mRatingBar.setRating(examHistory.getDifficulty());
        holder.mTxtProgress.setText(examHistory.getAlready_answer() + "/" + examHistory.getTotal());
        holder.mTxtAlreadyAnswer.setText(examHistory.getAnswer_number() + "人已作答");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExamActivity.class);
                intent.putExtra(ExamActivity.TITLE, examHistory.getName());
                intent.putExtra(ExamActivity.EXAM_TYPE, 2);
                intent.putExtra(ExamActivity.EXAM_PAGER_ID, examHistory.getId());
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_exam_title) TextView mTxtExamTitle;
        @BindView(R.id.ratingBar) RatingBar mRatingBar;
        @BindView(R.id.txt_progress) TextView mTxtProgress;
        @BindView(R.id.txt_already_answer) TextView mTxtAlreadyAnswer;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
