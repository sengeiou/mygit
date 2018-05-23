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
import com.beanu.l4_clean.model.bean.RankPeopleHeader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 排行榜 顶部
 * Created by Beanu on 2018/2/11.
 */
public class RankPeopleHeaderViewBinder extends ItemViewBinder<RankPeopleHeader, RankPeopleHeaderViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_rank_people_header, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RankPeopleHeader rankPeopleHeader) {

        Context context = holder.itemView.getContext();

        if (rankPeopleHeader.getRankPeople1() != null) {
            RankPeople rankPeople = rankPeopleHeader.getRankPeople1();
            if (!TextUtils.isEmpty(rankPeople.getIcon())) {
                Glide.with(context).load(rankPeople.getIcon()).apply(new RequestOptions().circleCrop()).into(holder.mImgHeader1);
            }
            holder.mTxtUserName1.setText(rankPeople.getNickname());
            holder.mTxtScore1.setText(rankPeople.getNum() + "");
        }

        if (rankPeopleHeader.getRankPeople2() != null) {
            RankPeople rankPeople = rankPeopleHeader.getRankPeople2();
            if (!TextUtils.isEmpty(rankPeople.getIcon())) {
                Glide.with(context).load(rankPeople.getIcon()).apply(new RequestOptions().circleCrop()).into(holder.mImgHeader2);
            }
            holder.mTxtUserName2.setText(rankPeople.getNickname());
            holder.mTxtScore2.setText(rankPeople.getNum() + "");
        }

        if (rankPeopleHeader.getRankPeople3() != null) {
            RankPeople rankPeople = rankPeopleHeader.getRankPeople3();
            if (!TextUtils.isEmpty(rankPeople.getIcon())) {
                Glide.with(context).load(rankPeople.getIcon()).apply(new RequestOptions().circleCrop()).into(holder.mImgHeader3);
            }
            holder.mTxtUserName3.setText(rankPeople.getNickname());
            holder.mTxtScore3.setText(rankPeople.getNum() + "");
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_header1) ImageView mImgHeader1;
        @BindView(R.id.img_header2) ImageView mImgHeader2;
        @BindView(R.id.img_header3) ImageView mImgHeader3;
        @BindView(R.id.txt_userName1) TextView mTxtUserName1;
        @BindView(R.id.txt_userName2) TextView mTxtUserName2;
        @BindView(R.id.txt_userName3) TextView mTxtUserName3;
        @BindView(R.id.txt_score1) TextView mTxtScore1;
        @BindView(R.id.txt_score2) TextView mTxtScore2;
        @BindView(R.id.txt_score3) TextView mTxtScore3;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
