package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.widget.LinearLayoutForListView;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.LiveLessonTeacherListAdapter;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.ui.common.TeacherDetailActivity;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonDetailActivity;
import com.beanu.l4_bottom_tab.util.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 直播课 ITEM
 * Created by Beanu on 2017/3/7.
 */
public class LiveLessonViewProvider
        extends ItemViewBinder<LiveLesson, LiveLessonViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_live_lesson, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final LiveLesson live_Lesson) {
        final Context context = holder.itemView.getContext();
        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, LiveLessonDetailActivity.class);
                intent.putExtra("lessonId", live_Lesson.getId());
                context.startActivity(intent);

            }
        });

        holder.mTxtTitle.setText(live_Lesson.getName());
        if (live_Lesson.getStart_time() != null && live_Lesson.getStart_time().length() >= 10) {
            holder.mTxtDate.setText(live_Lesson.getStart_time().replaceAll("-", ".").substring(0, 10));
        }
        if (live_Lesson.getEnd_time() != null && live_Lesson.getEnd_time().length() >= 10) {
            holder.mTxtDate.append(" - " + live_Lesson.getEnd_time().replaceAll("-", ".").substring(0, 10));
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
        if (live_Lesson.getPrice() == 0) {
            holder.mTxtPrice.setText("免费");
            holder.mTxtBuyer.setText(String.format("已有%s人领取", live_Lesson.getSale_num()));
        } else {
//            holder.mTxtPrice.setText("¥" + live_Lesson.getPrice());
            holder.mTxtPrice.setText(Html.fromHtml("<small>¥</small><font>" + live_Lesson.getPrice() + "</font>"));
            holder.mTxtBuyer.setText(String.format("已有%s人购买", live_Lesson.getSale_num()));
        }

        LiveLessonTeacherListAdapter teacherListAdapter = new LiveLessonTeacherListAdapter(context, live_Lesson.getTeacherList());
        holder.mListView.setOnItemClickLinstener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = view.getId();

                Intent intent = new Intent(context, TeacherDetailActivity.class);
                intent.putExtra("id", live_Lesson.getTeacherList().get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.mListView.setAdapter(teacherListAdapter);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_live_lesson_title) TextView mTxtTitle;
        @BindView(R.id.txt_live_lesson_date) TextView mTxtDate;
        @BindView(R.id.txt_live_lesson_days) TextView mTxtDays;
        @BindView(R.id.txt_live_lesson_price) TextView mTxtPrice;
        @BindView(R.id.txt_live_lesson_buyer) TextView mTxtBuyer;
        @BindView(R.id.ll_live_lesson_item) LinearLayout mLlItem;
        @BindView(R.id.list_view) LinearLayoutForListView mListView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}