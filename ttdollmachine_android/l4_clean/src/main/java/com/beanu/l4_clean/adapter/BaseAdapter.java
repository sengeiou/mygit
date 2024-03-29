package com.beanu.l4_clean.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.beanu.arad.support.recyclerview.adapter._BaseAdapter;

import java.util.List;

public abstract class BaseAdapter<E, VH extends RecyclerView.ViewHolder> extends _BaseAdapter<E, RecyclerView.ViewHolder> {

    protected LayoutInflater inflater;
    protected Context mContext;

    public BaseAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public BaseAdapter(Context context, List<E> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.mContext = context;
    }


}
