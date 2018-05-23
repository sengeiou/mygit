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
import com.beanu.l4_clean.model.bean.ShowClaw;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by jrl on 2017/11/23.
 */
public class ShowClawViewBinder extends ItemViewBinder<ShowClaw, ShowClawViewBinder.ViewHolder> {

    private Context mContext;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        mContext = parent.getContext();
        View root = inflater.inflate(R.layout.item_show_claw, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ShowClaw showClaw) {
        if (!TextUtils.isEmpty(showClaw.getImage())) {
            Glide.with(mContext)
                    .load(showClaw.getImage())
                    .into(holder.mImageClaw);
        }
        holder.mTextName.setText(showClaw.getName());
        holder.mTextNum.setText("×1");
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_claw)
        ImageView mImageClaw;
        @BindView(R.id.text_name)
        TextView mTextName;
        @BindView(R.id.text_num)
        TextView mTextNum;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
