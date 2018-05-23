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

import com.beanu.arad.utils.ToastUtils;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Anchor;
import com.beanu.l4_clean.ui.anchor.AnchorPlayActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 主播列表 Item
 * Created by Beanu on 2017/11/9.
 */
public class AnchorViewBinder extends ItemViewBinder<Anchor, AnchorViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_anchor, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Anchor anchor) {

        final Context context = holder.itemView.getContext();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (anchor.getIsLive() == 1) {
                    AnchorPlayActivity.startActivity(context, anchor.getRoomId(), anchor.getLogId());
                } else {
                    ToastUtils.showShort("主播开小差去了");
                }

            }
        });

        if (!TextUtils.isEmpty(anchor.getAnchorCover())) {
            Glide.with(context).load(anchor.getAnchorCover()).into(holder.mImgFace);
        }
        holder.mTxtNum.setText("抓中" + anchor.getSuccessNum() + "次");
        //TODO 改成融云获取
        holder.mTxtViews.setText("100人观看");
        holder.mTxtAnchorName.setText(anchor.getAnchorIdNickName());
        holder.mTxtAnchorSign.setText(anchor.getSignature());
        if (anchor.getIsLive() == 1) {
            holder.mImgStatus.setImageResource(R.drawable.online_111);
        } else {
            holder.mImgStatus.setImageResource(R.drawable.rest_111);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_face) ImageView mImgFace;
        @BindView(R.id.txt_num) TextView mTxtNum;
        @BindView(R.id.txt_views) TextView mTxtViews;
        @BindView(R.id.txt_anchor_name) TextView mTxtAnchorName;
        @BindView(R.id.txt_anchor_sign) TextView mTxtAnchorSign;
        @BindView(R.id.img_status) ImageView mImgStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}