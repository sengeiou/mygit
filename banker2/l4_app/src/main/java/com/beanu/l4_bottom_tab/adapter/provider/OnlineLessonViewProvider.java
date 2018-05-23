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
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.support.glide.transformations.RoundedCornersTransformation;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.ui.module3_onlineLesson.OnlineLessonDetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 高清网课
 * Created by Beanu on 2017/3/8.
 */
public class OnlineLessonViewProvider
        extends ItemViewBinder<OnlineLesson, OnlineLessonViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_online_lesson, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final OnlineLesson onlineLesson) {
        Context context = holder.itemView.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context mContext = holder.itemView.getContext();
                Intent intent = new Intent(mContext, OnlineLessonDetailActivity.class);
                intent.putExtra("lessonId", onlineLesson.getId());
                mContext.startActivity(intent);
            }
        });

        if (!TextUtils.isEmpty(onlineLesson.getCover_app())) {
            Glide.with(context)
                    .load(onlineLesson.getCover_app())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, 8, 0, RoundedCornersTransformation.CornerType.TOP)))
                    .into(holder.mImgOnlineLessonContent);
        }

        holder.mTxtOnlineLessonTitle.setText(onlineLesson.getName());
        if (!TextUtils.isEmpty(onlineLesson.getLong_time())) {
            holder.mTxtOnlineLessonTime.setText(onlineLesson.getLong_time() + "." + onlineLesson.getSale_num() + "次观看");
        } else {
            holder.mTxtOnlineLessonTime.setText(onlineLesson.getSale_num() + "次观看");
        }

        if (onlineLesson.getIs_charges() == 1) {
            holder.mImgTag.setVisibility(View.GONE);
            holder.mTxtOnlineLessonPrice.setVisibility(View.VISIBLE);
            holder.mTxtOnlineLessonPrice.setText(Html.fromHtml("<small>¥</small><strong style='color:red;'>" + onlineLesson.getPrice() + "</strong>"));
        } else {
            holder.mImgTag.setVisibility(View.VISIBLE);
            holder.mTxtOnlineLessonPrice.setVisibility(View.VISIBLE);
            holder.mTxtOnlineLessonPrice.setText("免费");

        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_online_lesson_content) ImageView mImgOnlineLessonContent;
        @BindView(R.id.txt_online_lesson_title) TextView mTxtOnlineLessonTitle;
        @BindView(R.id.txt_online_lesson_time) TextView mTxtOnlineLessonTime;
        @BindView(R.id.txt_online_lesson_price) TextView mTxtOnlineLessonPrice;
        @BindView(R.id.img_online_lesson_tag) ImageView mImgTag;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}