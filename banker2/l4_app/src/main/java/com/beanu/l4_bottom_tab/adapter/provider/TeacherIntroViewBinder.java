package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.beanu.l4_bottom_tab.ui.common.TeacherDetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 老师介绍
 * Created by Beanu on 2017/3/30.
 */
public class TeacherIntroViewBinder extends ItemViewBinder<TeacherIntro, TeacherIntroViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_teacher_intro, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final TeacherIntro teacherIntro) {
        final Context context = holder.itemView.getContext();
        holder.mTxtTeacherName.setText(String.format("主讲老师：%s", teacherIntro.getName()));
        holder.mTxtDuration.setText(String.format("共授课%s小时", teacherIntro.getTeaching_time()));

        if(!TextUtils.isEmpty(teacherIntro.getIntro())){

            RichText.fromHtml(teacherIntro.getIntro()).into(holder.mTxtIntro);
        }
        holder.mRatingBar.setRating(teacherIntro.getStar_rating());
        if (!TextUtils.isEmpty(teacherIntro.getHead_portrait())) {
            Glide.with(context).load(teacherIntro.getHead_portrait()).apply(RequestOptions.circleCropTransform()).into(holder.mImgAvatar);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TeacherDetailActivity.class);
                intent.putExtra("id", teacherIntro.getId());
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_avatar) ImageView mImgAvatar;
        @BindView(R.id.txt_teacher_name) TextView mTxtTeacherName;
        @BindView(R.id.ratingBar) RatingBar mRatingBar;
        @BindView(R.id.txt_duration) TextView mTxtDuration;
        @BindView(R.id.imageView4) ImageView mImageView4;
        @BindView(R.id.txt_intro) TextView mTxtIntro;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
