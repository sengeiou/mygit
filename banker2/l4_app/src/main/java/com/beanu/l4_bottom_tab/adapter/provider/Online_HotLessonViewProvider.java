package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.ui.module3_onlineLesson.OnlineLessonDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 高清网课 热门
 * Created by Beanu on 2017/3/8.
 */
public class Online_HotLessonViewProvider
        extends ItemViewBinder<Online_HotLesson, Online_HotLessonViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_online_hot_lesson, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Online_HotLesson online_HotLesson) {
        final Context mContext = holder.itemView.getContext();

        holder.mTxtTitle.setText(online_HotLesson.getName());
        if (online_HotLesson.getClassNum() > 0) {
            holder.mTxtDesc.setText("课时：" + online_HotLesson.getClassNum() + "节");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OnlineLessonDetailActivity.class);
                intent.putExtra("lessonId", online_HotLesson.getId());
                mContext.startActivity(intent);
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_title) TextView mTxtTitle;
        @BindView(R.id.txt_desc) TextView mTxtDesc;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}