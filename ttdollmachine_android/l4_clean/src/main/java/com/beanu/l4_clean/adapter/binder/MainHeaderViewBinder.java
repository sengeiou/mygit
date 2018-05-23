package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.MainHeader;
import com.beanu.l4_clean.ui.anchor.AnchorListActivity;
import com.beanu.l4_clean.ui.pk.ChallengeActivity;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Beanu on 2017/11/8.
 */

@Deprecated
public class MainHeaderViewBinder extends ItemViewBinder<MainHeader, MainHeaderViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_main_header, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MainHeader mainHeader) {
        final Context context = holder.itemView.getContext();

        holder.mImgAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AnchorListActivity.class));
            }
        });
        holder.mImgPk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ChallengeActivity.class));
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.banner) Banner mBanner;
        @BindView(R.id.img_anchor) ImageView mImgAnchor;
        @BindView(R.id.img_pk) ImageView mImgPk;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
