package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.CrawlRecord;
import com.beanu.l4_clean.ui.user.RecordDetailActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 抓取记录ITEM
 * Created by Beanu on 2018/2/22.
 */
public class CrawlRecordViewBinder extends ItemViewBinder<CrawlRecord, CrawlRecordViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_crawl_record, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final CrawlRecord crawlRecord) {
        final Context context = holder.itemView.getContext();
        if (!TextUtils.isEmpty(crawlRecord.getImage_cover())) {
            Glide.with(context).load(crawlRecord.getImage_cover()).into(holder.mImgDoll);
        }
        holder.mTxtTitle.setText(crawlRecord.getName());
        holder.mTxtTime.setText(crawlRecord.getCreatetime());

        if (crawlRecord.getIsSucceed() == 0) {
            holder.mImgStatus.setImageResource(R.drawable.zhua_lose_);
        } else {
            holder.mImgStatus.setImageResource(R.drawable.zhua_success_);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecordDetailActivity.class);
                intent.putExtra("bean", crawlRecord);
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_doll) ImageView mImgDoll;
        @BindView(R.id.txt_title) TextView mTxtTitle;
        @BindView(R.id.txt_time) TextView mTxtTime;
        @BindView(R.id.img_status) ImageView mImgStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}