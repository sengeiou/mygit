package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Dolls;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 同款列表
 */
public class SameDollsViewBinder extends ItemViewBinder<Dolls, SameDollsViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_same_dolls, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Dolls sameDolls) {

        Context context = holder.itemView.getContext();

        if (!TextUtils.isEmpty(sameDolls.getImage_cover())) {
            Glide.with(context).load(sameDolls.getImage_cover()).into(holder.mImgDoll);
        }

        if (sameDolls.getGame_status() == 0) {
            holder.mImgState.setImageResource(R.drawable.state_free);
        } else {
            holder.mImgState.setImageResource(R.drawable.state_gaming);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_doll) ImageView mImgDoll;
        @BindView(R.id.img_state) ImageView mImgState;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}