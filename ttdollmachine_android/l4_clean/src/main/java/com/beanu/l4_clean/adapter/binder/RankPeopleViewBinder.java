package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.RankPeople;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 排行榜
 * Created by Beanu on 2018/2/11.
 */
public class RankPeopleViewBinder extends ItemViewBinder<RankPeople, RankPeopleViewBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_rank_people, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RankPeople rankPeople) {

        Context context = holder.itemView.getContext();

        if (!TextUtils.isEmpty(rankPeople.getIcon())) {
            Glide.with(context).load(rankPeople.getIcon()).apply(new RequestOptions().circleCrop()).into(holder.mImgAvatar);
        }
        holder.mTxtName.setText(rankPeople.getNickname());
        holder.mTxtScore.setText(rankPeople.getNum() + "");
        holder.mTxtNum.setText(getPosition(holder) + 3);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_num) TextView mTxtNum;
        @BindView(R.id.img_avatar) ImageView mImgAvatar;
        @BindView(R.id.txt_name) TextView mTxtName;
        @BindView(R.id.txt_score) TextView mTxtScore;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}