package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.utils.SizeUtils;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.DeliverOrder;
import com.beanu.l4_clean.model.bean.ShowClaw;
import com.beanu.l4_clean.ui.user.OrderActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by jrl on 2017/11/23.
 */
public class DeliverOrderViewBinder extends ItemViewBinder<DeliverOrder, DeliverOrderViewBinder.ViewHolder> {

    private Context mContext;
    private int imageWidth = SizeUtils.dp2px(120);
    private int imageHeight = SizeUtils.dp2px(90);

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        mContext = parent.getContext();
        View root = inflater.inflate(R.layout.item_deliver_order, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final DeliverOrder deliverOrder) {

        String dollName = "";
        if (deliverOrder.getDollList() != null && deliverOrder.getDollList().size() > 0) {
            dollName = deliverOrder.getDollList().get(0).getName();
        }
        holder.mTextContent.setText(dollName + "等" + deliverOrder.getNumber() + "件奖品");

        switch (deliverOrder.getStatus()) {
            case 0:
                holder.mTextStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dingdan_time, 0, 0, 0);
                holder.mTextStatus.setText("待发货");
                break;
            case 1:
                holder.mTextStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dingdan_fahuo, 0, 0, 0);
                holder.mTextStatus.setText("已发货");
                break;
            case 2:
                holder.mTextStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dingdan_shouhuo, 0, 0, 0);
                holder.mTextStatus.setText("已收货");
                break;
        }

        holder.mClawList.removeAllViews();
        for (ShowClaw showClaw : deliverOrder.getDollList()) {
            ImageView imageView = createImageView(showClaw.getImage());
            holder.mClawList.addView(imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderActivity.startActivity(mContext, deliverOrder.getOrderId());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_status)
        ImageView mImageStatus;
        @BindView(R.id.text_status)
        TextView mTextStatus;
        @BindView(R.id.text_content)
        TextView mTextContent;
        @BindView(R.id.claw_list)
        LinearLayout mClawList;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ImageView createImageView(String url) {
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(imageWidth, imageHeight));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(10, 0, 10, 0);
        if (!TextUtils.isEmpty(url)) {
            Glide.with(mContext).load(url).into(imageView);
        }
        return imageView;
    }

}
