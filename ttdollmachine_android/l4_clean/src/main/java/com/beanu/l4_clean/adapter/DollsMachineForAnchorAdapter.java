package com.beanu.l4_clean.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Dolls;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主播选用的机器列表
 * Created by Beanu on 2017/11/13.
 */

public class DollsMachineForAnchorAdapter extends BaseAdapter<Dolls, DollsMachineForAnchorAdapter.ViewHolder> {

    private int mSelectedPos = -1;//保存当前选中的position

    public DollsMachineForAnchorAdapter(Context context, List<Dolls> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_dolls_machine_anchor, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Dolls dolls = list.get(position);
        ((ViewHolder) holder).bind(dolls, position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            ((ViewHolder) holder).mCheckBox.setChecked(mSelectedPos == position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_doll) ImageView mImgDoll;
        @BindView(R.id.txt_name) TextView mTxtName;
        @BindView(R.id.txt_price) TextView mTxtPrice;
        @BindView(R.id.radioButton) CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Dolls dolls, final int position) {
            if (!TextUtils.isEmpty(dolls.getImage_cover())) {
                Glide.with(mContext).load(dolls.getImage_cover()).into(mImgDoll);
            }

            mTxtName.setText(dolls.getName());
            mTxtPrice.setText(dolls.getPrice() + "币/次");

            mCheckBox.setChecked(mSelectedPos == position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mSelectedPos != position) {//当前选中的position和上次选中不是同一个position 执行
                        mCheckBox.setChecked(true);
                        if (mSelectedPos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                            notifyItemChanged(mSelectedPos, 0);
                        }
                        mSelectedPos = position;
                    }

                }
            });


        }

    }


    public int getSelectedPos() {
        return mSelectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
    }
}
