package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.MyDollsHeader;
import com.beanu.l4_clean.ui.user.MyRecordListActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Beanu on 2017/11/10.
 */
public class MyDollsHeaderViewBinder extends ItemViewBinder<MyDollsHeader, MyDollsHeaderViewBinder.ViewHolder> implements View.OnClickListener {

    public static final int NOT_DELIVER = 0;
    public static final int DELIVER = 1;

    private int tab;
    private OnTabClickListener mListener;

    public MyDollsHeaderViewBinder(int tab, OnTabClickListener listener) {
        this.tab = tab;
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_my_dolls_header, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull MyDollsHeader myDollsHeader) {
        holder.mBtnNotDeliver.setSelected(tab == NOT_DELIVER);
        holder.mBtnDeliver.setSelected(tab == DELIVER);

        holder.mBtnDeliver.setOnClickListener(tab == NOT_DELIVER ? this : null);
        holder.mBtnNotDeliver.setOnClickListener(tab == DELIVER ? this : null);

        holder.mBtnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, MyRecordListActivity.class);
                context.startActivity(intent);
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(AppHolder.getInstance().user.getIcon())
                .apply(new RequestOptions().error(R.drawable.base_head_default).circleCrop())
                .into(holder.mImgUserHeader);
        holder.mTxtUserName.setText(AppHolder.getInstance().user.getNickname());
        holder.mTxtHit.setText(String.format("共抓中%s次", myDollsHeader.getTotal()));
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onTabClick(tab);
        }
    }

    public interface OnTabClickListener {
        void onTabClick(int tab);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_user_header)
        ImageView mImgUserHeader;
        @BindView(R.id.txt_user_name)
        TextView mTxtUserName;
        @BindView(R.id.txt_hit)
        TextView mTxtHit;
        @BindView(R.id.btn_not_deliver)
        LinearLayout mBtnNotDeliver;
        @BindView(R.id.btn_deliver)
        LinearLayout mBtnDeliver;
        @BindView(R.id.btn_game_record)
        ImageView mBtnRecord;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
