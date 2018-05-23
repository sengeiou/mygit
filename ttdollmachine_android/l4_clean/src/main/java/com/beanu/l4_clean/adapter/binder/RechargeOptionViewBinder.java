package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.RechargeOption;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 充值选项
 * Created by jrl on 2017/11/20.
 */
public class RechargeOptionViewBinder extends ItemViewBinder<RechargeOption, RechargeOptionViewBinder.ViewHolder> {

    private DecimalFormat df = new DecimalFormat("#.##");
    private Context mContext;
    private OnRechargeSelectedListener mSelectedListener;

    public RechargeOptionViewBinder(OnRechargeSelectedListener selectedListener) {
        mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_recharge_option, parent, false);
        mContext = root.getContext();
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final RechargeOption rechargeOption) {
        holder.mCurrency.setText(rechargeOption.getCurrency() + "T币");
        holder.mPrice.setText("¥ " + df.format(rechargeOption.getPrice()));
        holder.mTxtDesc.setText("赠送" + rechargeOption.getGive_coins() + "张免费卡");

        if (rechargeOption.isSelected()) {
            holder.mPrice.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        } else {
            holder.mPrice.setTextColor(ContextCompat.getColor(mContext, R.color.color_price));
        }

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onItemSelected(rechargeOption, rechargeOption.isSelected());
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.currency)
        TextView mCurrency;
        @BindView(R.id.price)
        TextView mPrice;
        @BindView(R.id.container)
        RelativeLayout mContainer;
        @BindView(R.id.textView19)
        TextView mTxtDesc;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRechargeSelectedListener {
        void onItemSelected(RechargeOption rechargeOption, boolean isSelected);
    }
}