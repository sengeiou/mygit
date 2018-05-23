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
import com.beanu.l4_bottom_tab.ui.module1_exam.ErrorAnalysisActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 做题记录
 * Created by Beanu on 2017/4/25.
 */
public class ToolsHistoryViewBinder extends ItemViewBinder<ToolsHistory, ToolsHistoryViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_history_record, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ToolsHistory toolsHistory) {
        final Context context = holder.itemView.getContext();

        holder.mTxtTitle.setText(toolsHistory.getName());
        holder.mTxtDesc.setText(toolsHistory.getCreatetime() + "，共" + toolsHistory.getAnswer_total() + "道题，答对" + toolsHistory.getAnswer_realy() + "道");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO 跳转的错题分析界面，应该是全部做完的跳转到错题分析界面，没有做完的话继续做题
                Intent intent = new Intent(context, ErrorAnalysisActivity.class);
                intent.putExtra(ErrorAnalysisActivity.TITLE, toolsHistory.getName());
                intent.putExtra(ErrorAnalysisActivity.ANALYSIS_TYPE, toolsHistory.getType());
                intent.putExtra(ErrorAnalysisActivity.ANALYSIS_ID, toolsHistory.getId());
                context.startActivity(intent);
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
