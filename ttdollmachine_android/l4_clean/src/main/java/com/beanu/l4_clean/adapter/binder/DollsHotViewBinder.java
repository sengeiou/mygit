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
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.ui.game.GameActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 娃娃机列表
 * Created by Beanu on 2017/11/8.
 */
public class DollsHotViewBinder extends ItemViewBinder<Dolls, DollsHotViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_dolls_hot, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Dolls dolls) {

        final Context context = holder.itemView.getContext();

        if (!TextUtils.isEmpty(dolls.getImage_cover())) {
            Glide.with(context).load(dolls.getImage_cover()).into(holder.mImgDolls);
        }

        holder.mTxtDollsName.setText(dolls.getName());
        holder.mTxtDollsPrice.setText("" + dolls.getPrice());
//        if (dolls.getGame_status() == 0) {
//            holder.mTxtDollsStatus.setImageResource(R.drawable.state_free);
//        } else {
//            holder.mTxtDollsStatus.setImageResource(R.drawable.state_gaming);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.startActivity(context, dolls.getId());
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_dolls) ImageView mImgDolls;
        @BindView(R.id.txt_dolls_name) TextView mTxtDollsName;
        @BindView(R.id.txt_dolls_price) TextView mTxtDollsPrice;
//        @BindView(R.id.img_dolls_status) ImageView mTxtDollsStatus;
        @BindView(R.id.img_tag) ImageView mTxtDollsTag;
        @BindView(R.id.img_pank) ImageView mImgPank;
        @BindView(R.id.txt_people) TextView mTxtPeople;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}