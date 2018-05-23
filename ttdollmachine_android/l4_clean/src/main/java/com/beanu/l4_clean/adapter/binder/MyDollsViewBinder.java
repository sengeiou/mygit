package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.MyDolls;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 我的娃娃
 * Created by Beanu on 2017/11/10.
 */
public class MyDollsViewBinder extends ItemViewBinder<MyDolls, MyDollsViewBinder.ViewHolder> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private List<MyDolls> selectedDolls = new ArrayList<>();

    private OnSelectedChangeListener mListener;
    private int mType;//0 其他用户  1 自己

    public MyDollsViewBinder(int type, OnSelectedChangeListener listener) {
        mListener = listener;
        mType = type;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_my_dolls, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MyDolls myDolls) {
        Context context = holder.itemView.getContext();

        if (mType == 0) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }

        holder.mCheckBox.setChecked(myDolls.isChecked());
        holder.mCheckBox.setTag(myDolls);
        holder.mCheckBox.setOnClickListener(this);

        holder.mTextName.setText(myDolls.getName());
        holder.mTextTime.setText("可兑换" + myDolls.getCoins() + "开心币  " + myDolls.getCreatetime());

        if (!TextUtils.isEmpty(myDolls.getImage())) {
            Glide.with(context).load(myDolls.getImage()).into(holder.mImageClaw);
        }
    }

    public List<MyDolls> getSelectedDolls() {
        return selectedDolls;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {
        MyDolls doll = (MyDolls) v.getTag();
        doll.setChecked(!doll.isChecked());
        if (doll.isChecked()) {
            selectedDolls.add(doll);
        } else {
            selectedDolls.remove(doll);
        }

        if (mListener != null) {
            mListener.onSelectedChanged(selectedDolls.size());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkBox) CheckBox mCheckBox;
        @BindView(R.id.text_name) TextView mTextName;
        @BindView(R.id.text_time) TextView mTextTime;
        @BindView(R.id.image_claw) ImageView mImageClaw;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSelectedChangeListener {
        void onSelectedChanged(int length);
    }
}