package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.TimeUtils;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Winners;
import com.beanu.l4_clean.ui.game.OtherPeopleActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 中奖者列表
 * Created by Beanu on 2017/11/14.
 */
public class WinnersViewBinder extends ItemViewBinder<Winners, WinnersViewBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_winners, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Winners winners) {
        int position = getPosition(holder) + 1;
        final Context context = holder.itemView.getContext();

            holder.mTxtNumber.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(winners.getCreatetime())) {
                String time = TimeUtils.getFriendlyTimeSpanByNow(winners.getCreatetime());
                holder.mTxtWinnerTime.setText(time);
            }


        if (!TextUtils.isEmpty(winners.getIcon())) {
            Glide.with(context).load(winners.getIcon()).apply(RequestOptions.circleCropTransform()).into(holder.mImgWinnerAvatar);
        }

        holder.mTxtWinnerName.setText(winners.getNickname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OtherPeopleActivity.class);
                context.startActivity(intent);
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_number) TextView mTxtNumber;
        @BindView(R.id.img_winner_avatar) ImageView mImgWinnerAvatar;
        @BindView(R.id.txt_winner_name) TextView mTxtWinnerName;
        @BindView(R.id.txt_winner_time) TextView mTxtWinnerTime;
        @BindView(R.id.img_number) ImageView mImgNumber;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
