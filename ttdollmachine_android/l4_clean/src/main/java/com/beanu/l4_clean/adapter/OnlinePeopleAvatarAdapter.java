package com.beanu.l4_clean.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 在线的人
 * Created by Beanu on 2017/11/13.
 */

public class OnlinePeopleAvatarAdapter extends BaseAdapter<OnlinePeople, OnlinePeopleAvatarAdapter.ViewHolder> {


    public OnlinePeopleAvatarAdapter(Context context, List<OnlinePeople> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_online_user_avatar, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        OnlinePeople onlinePeople = list.get(position);
        Glide.with(mContext).load(onlinePeople.getHeader()).apply(RequestOptions.circleCropTransform()).into(((ViewHolder) holder).mImgUserAvatar);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_user_avatar) ImageView mImgUserAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
