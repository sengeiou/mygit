package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonDetailActivity;
import com.beanu.l4_bottom_tab.util.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 老师详情 在售 直播课
 * Created by Beanu on 2017/3/7.
 */
public class TeacherWithLiveLessonViewProvider
        extends ItemViewBinder<LiveLesson, TeacherWithLiveLessonViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_teacher_with_live_lesson, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LiveLesson live_Lesson) {

        final Context context = holder.itemView.getContext();
        final String lessonId = live_Lesson.getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, LiveLessonDetailActivity.class);
                intent.putExtra("lessonId", lessonId);
                context.startActivity(intent);

            }
        });

        holder.mTxtTitle.setText(live_Lesson.getName());

        try {
            if (live_Lesson.getStart_time() != null)
                holder.mTxtDate.setText(live_Lesson.getStart_time().replaceAll("-", ".").substring(0, 10) + " - " + live_Lesson.getEnd_time().replaceAll("-", ".").substring(0, 10));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (live_Lesson.getStop_sale() != null) {

            String stopTime = TimeUtils.countDownDays(live_Lesson.getStop_sale());

            if (!TextUtils.isEmpty(stopTime)) {
                holder.mTxtDays.setText(String.format("距离停售%s", stopTime));
            } else {
                holder.mTxtDays.setText("已停售");
            }
        } else {
            holder.mTxtDays.setText("");
        }

        holder.mTxtPrice.setText("¥" + live_Lesson.getPrice());
        holder.mTxtBuyer.setText(String.format("已有%s人购买", live_Lesson.getSale_num()));

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_live_lesson_title) TextView mTxtTitle;
        @BindView(R.id.txt_live_lesson_date) TextView mTxtDate;
        @BindView(R.id.txt_live_lesson_days) TextView mTxtDays;
        @BindView(R.id.txt_live_lesson_price) TextView mTxtPrice;
        @BindView(R.id.txt_live_lesson_buyer) TextView mTxtBuyer;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}