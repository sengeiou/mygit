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
import com.beanu.l4_clean.model.bean.Message;
import com.beanu.l4_clean.ui.WebActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 我的消息
 * Created by Beanu on 2017/11/10.
 */
public class MessageViewBinder extends ItemViewBinder<Message, MessageViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Message message) {

        final Context context = holder.itemView.getContext();
        holder.mTxtMessageTitle.setText(message.getTitle());

        if (message.getType() == 0) {
            holder.mTxtMessageInfo2.setVisibility(View.GONE);
            holder.mImgMessage.setVisibility(View.GONE);

//            RichText.fromHtml(message.getContent()).into(holder.mTxtMessageInfo);
        } else {
            holder.mTxtMessageInfo2.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(message.getImages())) {
                holder.mImgMessage.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getImages()).into(holder.mImgMessage);
            }

//            RichText.fromHtml(message.getContent()).into(holder.mTxtMessageInfo2);
            holder.mTxtMessageInfo.setText("阅读更多");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.startActivity(context, message.getUrl(), message.getTitle());
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_message_title) TextView mTxtMessageTitle;
        @BindView(R.id.img_message) ImageView mImgMessage;
        @BindView(R.id.txt_message_info2) TextView mTxtMessageInfo2;
        @BindView(R.id.txt_message_info) TextView mTxtMessageInfo;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
