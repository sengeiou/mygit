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

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Commodity;
import com.beanu.l4_clean.ui.shop.ShopDetailActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 积分商城
 * Created by Beanu on 2018/3/10.
 */
public class CommodityViewBinder extends ItemViewBinder<Commodity, CommodityViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_commodity, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Commodity commodity) {
        final Context context = holder.itemView.getContext();

        if (!TextUtils.isEmpty(commodity.getImage())) {
            Glide.with(context).load(commodity.getImage()).into(holder.mImgCommodity);
        }
        holder.mTxtTitle.setText(commodity.getName());
        holder.mTxtJifen.setText(commodity.getScore() + "积分");
        holder.mTxtStock.setText("剩余：" + commodity.getStock());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopDetailActivity.class);
                intent.putExtra("id", commodity.getId());
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_commodity) ImageView mImgCommodity;
        @BindView(R.id.txt_title) TextView mTxtTitle;
        @BindView(R.id.txt_jifen) TextView mTxtJifen;
        @BindView(R.id.txt_stock) TextView mTxtStock;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}