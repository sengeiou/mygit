package com.beanu.l4_clean.adapter.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.TaskHeader;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Beanu on 2018/2/23.
 */
public class TaskHeaderViewBinder extends ItemViewBinder<TaskHeader, TaskHeaderViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_task_header, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TaskHeader taskHeader) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
