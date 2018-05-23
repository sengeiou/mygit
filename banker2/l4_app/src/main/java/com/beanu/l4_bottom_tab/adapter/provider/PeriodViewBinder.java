package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.model.bean.Period;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 高清网课  课时
 * Created by Beanu on 2017/3/31.
 */
public class PeriodViewBinder extends ItemViewBinder<Period, PeriodViewBinder.ViewHolder> {

    private PeriodClickEvent mClickEvent;
    private OnlineLesson mOnlineLesson;

    public interface PeriodClickEvent {
        public void onClick(int position);

        public void onPay();
    }

    public PeriodViewBinder(OnlineLesson onlineLesson, PeriodClickEvent clickEvent) {
        mClickEvent = clickEvent;
        mOnlineLesson = onlineLesson;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_online_lesson_period, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Period period) {

        Context context = holder.itemView.getContext();

        holder.mTxtTitle.setText(period.getName());
        if (period.getIsTry() == 1) {
            holder.mTxtFree.setVisibility(View.VISIBLE);
        } else {
            holder.mTxtFree.setVisibility(View.GONE);
        }

        if (isUserCharged()) {
            holder.mTxtFree.setVisibility(View.GONE);
        }
        holder.mTxtDesc.setText("主讲：" + period.getTeacher() + "   时长：" + period.getLongTime());

        if (period.isPlaying()) {
            holder.mImgPlay.setImageResource(R.drawable.play_2);

            holder.mTxtTitle.setTextColor(context.getResources().getColor(R.color.period_playing));
            holder.mTxtDesc.setTextColor(context.getResources().getColor(R.color.period_playing));

        } else {
            holder.mImgPlay.setImageResource(R.drawable.play_1);

            holder.mTxtTitle.setTextColor(context.getResources().getColor(R.color.period_title));
            holder.mTxtDesc.setTextColor(context.getResources().getColor(R.color.period_desc));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((period.getIsTry() == 1 || isUserCharged())) {
                    mClickEvent.onClick(getPosition(holder));
                } else {
                    mClickEvent.onPay();
                }
            }
        });

    }

    private boolean isUserCharged() {
        if (mOnlineLesson != null) {
            if (mOnlineLesson.getIs_charges() == 0 || mOnlineLesson.getIsBuy() == 1) {
                return true;
            }
        }
        return false;
    }

//    private boolean isExprity() {
//        if (mOnlineLesson != null && mOnlineLesson.getIsTime() == 1) {
//            return false;
//        }
//        return true;
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_title) TextView mTxtTitle;
        @BindView(R.id.txt_free) ImageView mTxtFree;
        @BindView(R.id.txt_date) TextView mTxtDesc;
        @BindView(R.id.img_play) ImageView mImgPlay;
        @BindView(R.id.rl_item) RelativeLayout mRlItem;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}