package com.beanu.l4_clean.adapter.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Coupon;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 优惠券
 * Created by Beanu on 2018/2/23.
 */
public class CouponViewBinder extends ItemViewBinder<Coupon, CouponViewBinder.ViewHolder> {

    int mType;

    public CouponViewBinder(int type) {
        mType = type;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Coupon coupon) {
        if (mType == 0) {
            holder.mImgBg.setImageResource(R.drawable.freecard_2);
            holder.mTxtType.setText("充值赠送");
        } else {
            holder.mImgBg.setImageResource(R.drawable.freecard_1);
            holder.mTxtType.setText("登录赠送");
        }

        holder.mTxtTitle.setText("免费卡");
        holder.mTxtDesc.setText("有效期：" + coupon.getExpire_time());
        holder.mTxtUser.setEnabled(true);
        holder.mTxtUsedTime.setText("");

        if (coupon.getIs_us() == 1) {
            holder.mImgBg.setImageResource(R.drawable.freecard_3);
            holder.mTxtUser.setEnabled(false);
            holder.mTxtUsedTime.setText(coupon.getUs_time() + "使用");
        }


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_bg) ImageView mImgBg;
        @BindView(R.id.txt_title) TextView mTxtTitle;
        @BindView(R.id.txt_type) TextView mTxtType;
        @BindView(R.id.textView5) TextView mTxtDesc;
        @BindView(R.id.txt_used_time) TextView mTxtUsedTime;


        @BindView(R.id.txt_use) TextView mTxtUser;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
