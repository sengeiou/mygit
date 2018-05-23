package com.beanu.l4_bottom_tab.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.ProvinceEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * 省份列表
 * Created by Beanu on 2017/5/3.
 */

public class ProvinceAdapter extends IndexableAdapter<ProvinceEntity> {


    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province_bar, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province, parent, false));
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        ((TitleViewHolder) holder).mTxtIndexTitle.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, ProvinceEntity entity) {
        ((ContentViewHolder) holder).mTxtTitle.setText(entity.getFieldIndexBy());
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_index_title) TextView mTxtIndexTitle;

        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title) TextView mTxtTitle;

        ContentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
