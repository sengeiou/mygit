package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.ui.module5_my.live_lesson.MyLiveLessonActivity;

import me.drakeet.multitype.ItemViewBinder;

/**
 * 直播课 -我的课程
 * Created by Beanu on 2017/3/7.
 */
public class Live_MyCourseViewProvider
        extends ItemViewBinder<Live_MyCourse, Live_MyCourseViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_live__my_course, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Live_MyCourse live_MyCourse) {
        final Context context = holder.itemView.getContext();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyLiveLessonActivity.class);
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}