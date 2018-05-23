package com.beanu.l4_clean.adapter.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 任务
 * Created by Beanu on 2018/2/23.
 */
public class TaskViewBinder extends ItemViewBinder<Task, TaskViewBinder.ViewHolder> {


    private OnClickListener mOnClickListener;

    public TaskViewBinder(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_task, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Task task) {
        holder.mTxtTaskDesc.setText(task.getTitle());
        holder.mTxtTaskScore.setText(task.getCoins());
        if (!task.isComplete()) {
            holder.mTxtTaskStatus.setText("去完成");
            holder.mTxtTaskStatus.setEnabled(true);
        } else {
            holder.mTxtTaskStatus.setText("已完成");
            holder.mTxtTaskStatus.setEnabled(false);
        }


        holder.mTxtTaskStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    if (task.getType() == 0) {
                        mOnClickListener.onClickPlayGame();
                    } else if (task.getType() == 1) {
                        mOnClickListener.onClickShare();
                    } else if (task.getType() == 2) {
                        mOnClickListener.onClickInvite();
                    }
                }
            }
        });


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_task_desc) TextView mTxtTaskDesc;
        @BindView(R.id.txt_task_score) TextView mTxtTaskScore;
        @BindView(R.id.txt_task_status) TextView mTxtTaskStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {
        public void onClickPlayGame();

        public void onClickShare();

        public void onClickInvite();
    }
}