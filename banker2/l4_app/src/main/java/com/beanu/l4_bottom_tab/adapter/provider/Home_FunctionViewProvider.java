package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.ui.module1_exam.ExamActivity;
import com.beanu.l4_bottom_tab.ui.module_news.NewsListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Beanu on 2017/2/28.
 */
public class Home_FunctionViewProvider
        extends ItemViewBinder<Home_Function, Home_FunctionViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_home_function, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Home_Function home_Function) {

        final Context context = holder.itemView.getContext();
        holder.mRlItemHomeFunctionPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExamActivity.class);
                intent.putExtra(ExamActivity.TITLE, "智能练习");
                intent.putExtra(ExamActivity.EXAM_TYPE, 1);
                context.startActivity(intent);
            }
        });


        holder.mRlItemHomeFunctionNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewsListActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_item_home_function_practice) RelativeLayout mRlItemHomeFunctionPractice;
        @BindView(R.id.rl_item_home_function_news) RelativeLayout mRlItemHomeFunctionNews;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}