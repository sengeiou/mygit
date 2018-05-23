package com.beanu.l4_clean.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.IMMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 聊天消息列表
 * Created by Beanu on 2017/11/14.
 */

public class IMMessageAdapter extends BaseAdapter<IMMessage, IMMessageAdapter.ViewHolder> {


    public IMMessageAdapter(Context context, List<IMMessage> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IMMessageAdapter.ViewHolder(inflater.inflate(R.layout.item_im_message, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        IMMessage imMessage = list.get(position);
        ((ViewHolder) holder).bind(imMessage);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_message_title) TextView mTxtTitle;
        @BindView(R.id.txt_message_info) TextView mTxtInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(IMMessage imMessage) {
            mTxtTitle.setText(imMessage.getUserName() + ":");
            mTxtInfo.setText(imMessage.getContent());
        }
    }
}

