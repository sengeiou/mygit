package com.beanu.l4_clean.adapter.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.FirendDollsHeader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 好友 基本信息
 * Created by Beanu on 2018/3/9.
 */
public class FirendDollsHeaderViewBinder extends ItemViewBinder<FirendDollsHeader, FirendDollsHeaderViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_firend_dolls_header, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull FirendDollsHeader firendDollsHeader) {
        Glide.with(holder.itemView.getContext())
                .load(firendDollsHeader.getIcon())
                .apply(new RequestOptions().error(R.drawable.base_head_default).circleCrop())
                .into(holder.mImgUserHeader);
        holder.mTxtUserName.setText(firendDollsHeader.getNickname());
        holder.mTxtHit.setText(String.format("共抓中%s次", firendDollsHeader.getSucNum()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user_header) ImageView mImgUserHeader;
        @BindView(R.id.txt_hit) TextView mTxtHit;
        @BindView(R.id.txt_user_name) TextView mTxtUserName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
