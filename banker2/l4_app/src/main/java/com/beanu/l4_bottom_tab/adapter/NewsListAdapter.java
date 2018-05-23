package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l3_common.adapter.BaseLoadMoreAdapter;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.NewsItem;
import com.beanu.l4_bottom_tab.ui.module_news.NewsDetailActivity;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 列表适配器
 * Created by Beanu on 2016/12/16.
 */

public class NewsListAdapter extends BaseLoadMoreAdapter<NewsItem, NewsListAdapter.ViewHolder> {

    public NewsListAdapter(Context context, List<NewsItem> list) {
        super(context, list);
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_news1, parent, false));
    }

    @Override
    public void onBindItemViewHolder(final ViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();

        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("newsId", getItem(position).getId() + "");
                context.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView mTxtTitle;
        private TextView mTxtDesc;
        private TextView mTxtLook;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_item_news);
            mTxtTitle = (TextView) itemView.findViewById(R.id.txt_item_news_title);
            mTxtDesc = (TextView) itemView.findViewById(R.id.txt_item_news_desc);
            mTxtLook = (TextView) itemView.findViewById(R.id.txt_item_news_look);
        }

        private void bind(NewsItem item) {
            Glide.with(mContext).load(item.getImgUrl()).into(img);
            mTxtTitle.setText(item.getTitle());
            mTxtDesc.setText(item.getShowTime());
            mTxtLook.setText(item.getClick() + "");
        }
    }
}