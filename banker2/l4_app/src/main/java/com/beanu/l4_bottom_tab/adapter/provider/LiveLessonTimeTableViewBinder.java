package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.LiveLessonTimeTable;
import com.beanu.l4_bottom_tab.model.bean.MyLiveLesson;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonPlayActivity;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonVodPlayActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 直播课 课程表
 * Created by Beanu on 2017/3/30.
 */
public class LiveLessonTimeTableViewBinder extends ItemViewBinder<LiveLessonTimeTable, LiveLessonTimeTableViewBinder.ViewHolder> {

    private int model;
    private MyLiveLesson mMyLiveLesson;

    public LiveLessonTimeTableViewBinder(int model, MyLiveLesson myLiveLesson) {
        this.model = model;
        this.mMyLiveLesson = myLiveLesson;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_live_lesson_time_table, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final LiveLessonTimeTable liveLessonTimeTable) {

        final Context context = holder.itemView.getContext();

        holder.mTxtTime.setText(liveLessonTimeTable.getEnd_time());
        holder.mTxtTitle.setText(liveLessonTimeTable.getName());
        if (model == 0) {
            //详情页课时列表
            holder.mImgPlay.setVisibility(View.GONE);
            holder.mImgPlayTag.setVisibility(View.GONE);
        } else {
            //我的课程 课时列表

            switch (liveLessonTimeTable.getState()) {
                case 0:
                    holder.mImgPlay.setVisibility(View.GONE);
                    holder.mImgPlayTag.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ToastUtils.showShort("直播还未开始");
                        }
                    });

                    break;
                case 1:
                    holder.mImgPlay.setVisibility(View.VISIBLE);
                    holder.mImgPlayTag.setVisibility(View.VISIBLE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //跳转到直播页面
                            if (mMyLiveLesson != null) {

                                LiveLessonPlayActivity.startActivity(context, liveLessonTimeTable.getLiveCode(), liveLessonTimeTable.getStuAppPsw(), AppHolder.getInstance().user.getNickname());
                            } else {
                                ToastUtils.showShort("初始化出错，请联系客服");
                            }

                        }
                    });
                    break;
                case 2:
                    holder.mImgPlay.setVisibility(View.VISIBLE);
                    holder.mImgPlayTag.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //跳转到录播页面
                            if (mMyLiveLesson != null) {
                                LiveLessonVodPlayActivity.startActivity(context, liveLessonTimeTable.getSdkId(), liveLessonTimeTable.getCode(), AppHolder.getInstance().user.getNickname(), mMyLiveLesson.getName());
                            } else {
                                ToastUtils.showShort("初始化出错，请联系客服");
                            }
                        }
                    });
                    break;
            }
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_date)
        TextView mTxtTime;
        @BindView(R.id.txt_title)
        TextView mTxtTitle;
        @BindView(R.id.img_play)
        ImageView mImgPlay;
        @BindView(R.id.img_play_tag)
        ImageView mImgPlayTag;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}