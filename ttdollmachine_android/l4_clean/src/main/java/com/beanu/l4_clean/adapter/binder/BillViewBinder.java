package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.BillItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 账单列表
 * Created by Beanu on 2017/11/10.
 */
public class BillViewBinder extends ItemViewBinder<BillItem, BillViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull BillItem bill) {
        Context context = holder.itemView.getContext();

        holder.mTxtBillTitle.setText(bill.getTypeName());
        holder.mTxtBillTime.setText(bill.getCreatetime());
        holder.mTxtBillNum.setText(bill.getCoins() + "");
        if (bill.getCoins() > 0) {
            holder.mTxtBillNum.setTextColor(context.getResources().getColor(R.color.text_coin_green));
        } else {
            holder.mTxtBillNum.setTextColor(context.getResources().getColor(R.color.text_coin_red));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_bill_title) TextView mTxtBillTitle;
        @BindView(R.id.txt_bill_time) TextView mTxtBillTime;
        @BindView(R.id.txt_bill_num) TextView mTxtBillNum;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}