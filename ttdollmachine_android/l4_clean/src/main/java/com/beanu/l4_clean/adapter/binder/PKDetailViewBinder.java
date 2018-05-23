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
import com.beanu.l4_clean.model.bean.PKDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by jrl on 2017/11/21.
 */
public class PKDetailViewBinder extends ItemViewBinder<PKDetail, PKDetailViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_p_k_detail, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PKDetail pKDetail) {
        Context context = holder.itemView.getContext();

        holder.mTextScene.setText(pKDetail.getStartTime());
        if (pKDetail.getResult() == 1) {
            if (!TextUtils.isEmpty(pKDetail.getIcon())) {
                Glide.with(context).load(pKDetail.getIcon()).apply(RequestOptions.circleCropTransform()).into(holder.mImgWinnerAvatar);
            }
            holder.mTextWinTitle.setText(pKDetail.getNickName() + "获胜");

        } else if (pKDetail.getResult() == 2) {

            if (!TextUtils.isEmpty(pKDetail.getChallengeIcon())) {
                Glide.with(context).load(pKDetail.getChallengeIcon()).apply(RequestOptions.circleCropTransform()).into(holder.mImgWinnerAvatar);
            }
            holder.mTextWinTitle.setText(pKDetail.getChallengeName() + "获胜");
        }

        if (!TextUtils.isEmpty(pKDetail.getImageCover())) {
            Glide.with(context).load(pKDetail.getImageCover()).apply(RequestOptions.circleCropTransform()).into(holder.mPrizeImg);
        }

        holder.mPrizeName.setText("获得" + pKDetail.getDollName());
        holder.mTextWinContent.setText(String.format("获得%s开心币奖励", pKDetail.getCurrency()));

        if (!TextUtils.isEmpty(pKDetail.getIcon())) {
            Glide.with(context).load(pKDetail.getIcon()).apply(RequestOptions.circleCropTransform()).into(holder.mMyImg);
        }
        holder.mMyPlay.setText(pKDetail.getNickName() + " 抓了" + pKDetail.getSelfNum() + "次");
        if (!TextUtils.isEmpty(pKDetail.getChallengeIcon())) {
            Glide.with(context).load(pKDetail.getChallengeIcon()).apply(RequestOptions.circleCropTransform()).into(holder.mOtherImg);
        }
        holder.mOtherPlay.setText(pKDetail.getChallengeName() + " 抓了" + pKDetail.getOppNum() + "次");


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_scene) TextView mTextScene;
        @BindView(R.id.img_winner_avatar) ImageView mImgWinnerAvatar;
        @BindView(R.id.text_win_title) TextView mTextWinTitle;
        @BindView(R.id.prize_img) ImageView mPrizeImg;
        @BindView(R.id.prize_name) TextView mPrizeName;
        @BindView(R.id.text_win_content) TextView mTextWinContent;
        @BindView(R.id.my_img) ImageView mMyImg;
        @BindView(R.id.my_play) TextView mMyPlay;
        @BindView(R.id.other_img) ImageView mOtherImg;
        @BindView(R.id.other_play) TextView mOtherPlay;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
