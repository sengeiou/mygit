package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.MyLiveLesson;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.live_lesson.MyLiveLessonPeriodActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 我的直播课
 * Created by Beanu on 2017/4/1.
 */
public class MyLiveLessonViewBinder extends ItemViewBinder<MyLiveLesson, MyLiveLessonViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_my_live_lesson, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MyLiveLesson myLiveLesson) {

        final Context context = holder.itemView.getContext();

        if (myLiveLesson.getTeacherList() != null && myLiveLesson.getTeacherList().size() > 0) {
            TeacherIntro teacherIntro = myLiveLesson.getTeacherList().get(0);
            if (!TextUtils.isEmpty(teacherIntro.getHead_portrait())) {
                Glide.with(context).load(teacherIntro.getHead_portrait()).apply(RequestOptions.circleCropTransform()).into(holder.mImgTeacher);
            }
            holder.mTxtTeacherName.setText(teacherIntro.getName());
        }
        holder.mTxtTitle.setText(myLiveLesson.getName());
        if (myLiveLesson.getStart_time() != null && myLiveLesson.getEnd_time() != null) {
            holder.mTxtDate.setText(myLiveLesson.getStart_time().replaceAll("-", ".") + " - " + myLiveLesson.getEnd_time().replaceAll("-", "."));
        }
        switch (myLiveLesson.getState()) {
            case 0:
                holder.mImgState.setImageResource(R.drawable.syllabus_label_grey);
                holder.mImageButton.setImageResource(R.drawable.syllabus_grey_bf);
                break;
            case 1:
                holder.mImgState.setImageResource(R.drawable.syllabus_label_g);
                holder.mImageButton.setImageResource(R.drawable.syllabus_yellow);
                break;
            case 2:
                holder.mImgState.setVisibility(View.GONE);
                holder.mTxtBuyerNum.setVisibility(View.GONE);
                holder.mImageButton.setVisibility(View.GONE);
                holder.mRlBottom.setVisibility(View.VISIBLE);
                break;
        }
        holder.mTxtBuyerNum.setText(String.format("已有%s人购买", myLiveLesson.getSale_num()));


        if (myLiveLesson.getState() == 2) {
            holder.mRatingBar.setRating(myLiveLesson.getStar_rating());
            holder.mTxtCommentNum.setText(myLiveLesson.getEvaluate_num() + "");
            holder.mTxtBuyerNum.setText(myLiveLesson.getSale_num() + "");//TODO 应该是查看数量

            holder.mTxtCommentNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        //点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (myLiveLesson.getState()) {
                    case 0:
                        Intent intent3 = new Intent(context, LiveLessonDetailActivity.class);
                        intent3.putExtra("lessonId", myLiveLesson.getId());
                        context.startActivity(intent3);

                        break;
                    case 1:

                        Intent intent = new Intent(context, MyLiveLessonPeriodActivity.class);
                        intent.putExtra("lessonId", myLiveLesson.getId());
                        intent.putExtra("title", myLiveLesson.getName());
                        intent.putExtra("lesson", myLiveLesson);
                        context.startActivity(intent);

                        break;
                    case 2:
                        Intent intent1 = new Intent(context, MyLiveLessonPeriodActivity.class);
                        intent1.putExtra("lessonId", myLiveLesson.getId());
                        intent1.putExtra("title", myLiveLesson.getName());
                        intent1.putExtra("lesson", myLiveLesson);

                        context.startActivity(intent1);
                        break;
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_teacher) ImageView mImgTeacher;
        @BindView(R.id.txt_teacher_name) TextView mTxtTeacherName;
        @BindView(R.id.txt_title) TextView mTxtTitle;
        @BindView(R.id.txt_date) TextView mTxtDate;
        @BindView(R.id.img_state) ImageView mImgState;
        @BindView(R.id.txt_buyer_num) TextView mTxtBuyerNum;
        @BindView(R.id.ratingBar) RatingBar mRatingBar;
        @BindView(R.id.txt_comment_num) TextView mTxtCommentNum;
        @BindView(R.id.txt_look_num) TextView mTxtLookNum;
        @BindView(R.id.rl_bottom) RelativeLayout mRlBottom;
        @BindView(R.id.imageButton) ImageButton mImageButton;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
