package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.utils.SizeUtils;
import com.beanu.l4_bottom_tab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 空白区域
 * Created by Beanu on 2017/2/28.
 */
public class SpaceViewProvider
        extends ItemViewBinder<Space, SpaceViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_space, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Space space) {

        Context context = holder.mSpace.getContext();
        if (space.getSpan() <= 0) {
            space.setSpan(16);
        }
        holder.mSpace.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(space.getSpan())));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.space) android.widget.Space mSpace;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}