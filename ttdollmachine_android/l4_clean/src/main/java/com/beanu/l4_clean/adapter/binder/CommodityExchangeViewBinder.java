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
import com.beanu.l4_clean.model.bean.Commodity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 兑换记录
 * Created by Beanu on 2017/11/10.
 */

public class CommodityExchangeViewBinder extends ItemViewBinder<Commodity, CommodityExchangeViewBinder.ViewHolder> {

    private Context mContext;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_commodity_exchange, parent, false);
        mContext = root.getContext();
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Commodity item) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PKDetailActivity.startActivity(mContext, item.getMatchId());
            }
        });

        holder.mTxtTime.setText("兑换时间：" + item.getCreatetime());
        if (!TextUtils.isEmpty(item.getImage())) {
            Glide.with(mContext).load(item.getImage()).into(holder.mImgCommodity);
        }

        holder.mTxtTitle.setText(item.getName());
        holder.mTxtJifen.setText(item.getScore() + "积分");

        switch (item.getStatus()) {
            case 0:
                holder.mTxtStatus.setText("待发货");
                break;
            case 1:
                holder.mTxtStatus.setText("已发货");
                break;
            case 2:
                holder.mTxtStatus.setText("已收货");
                break;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_commodity) ImageView mImgCommodity;
        @BindView(R.id.txt_title) TextView mTxtTitle;
        @BindView(R.id.txt_jifen) TextView mTxtJifen;
        @BindView(R.id.txt_time) TextView mTxtTime;
        @BindView(R.id.txt_status) TextView mTxtStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}