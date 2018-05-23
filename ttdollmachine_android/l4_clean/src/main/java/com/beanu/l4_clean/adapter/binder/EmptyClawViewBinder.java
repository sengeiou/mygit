package com.beanu.l4_clean.adapter.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.EmptyClaw;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by jrl on 2017/11/23.
 */
public class EmptyClawViewBinder extends ItemViewBinder<EmptyClaw, EmptyClawViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_empty_claw, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull EmptyClaw emptyClaw) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
