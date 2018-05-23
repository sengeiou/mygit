package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.beanu.l3_common.adapter.BaseAdapter;
import com.beanu.l4_bottom_tab.R;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择图片
 * Created by lizhihua on 2017/3/6.
 */

public class PhotoGridAdapter extends BaseAdapter<String, PhotoGridAdapter.ViewHolder> {
    private Map<String, Boolean> status = new HashMap<>();

    public PhotoGridAdapter(Context context, List<String> list) {
        super(context, list);
    }

    public void setLoadingStatus(String item, boolean isLoading) {
        status.put(item, isLoading);
        notifyDataSetChanged();
    }

    public void clearStatus() {
        status.clear();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_pick_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        ViewHolder holder = ((ViewHolder) h);
        if (position < super.getItemCount()) {
            String url = list.get(position);
            Glide.with(mContext).load(url).into(holder.image);
            Boolean isLoading = status.get(url);
            if (isLoading != null && isLoading) {
                holder.progress.setVisibility(View.VISIBLE);
            } else {
                holder.progress.setVisibility(View.GONE);
            }
        } else {
            Glide.with(mContext).load(R.drawable.default_img).into(holder.image);
            holder.progress.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.progress)
        ProgressBar progress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
