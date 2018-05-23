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
import com.beanu.l4_clean.model.bean.HelpMeCraw;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 帮我抓列表
 * Created by Beanu on 2017/12/18.
 */
public class HelpMeCrawViewBinder extends ItemViewBinder<HelpMeCraw, HelpMeCrawViewBinder.ViewHolder> {


    public interface OnHelpClickListener {
        public void onHelpCLick(int position);
    }

    private OnHelpClickListener mClickListener;

    public HelpMeCrawViewBinder(OnHelpClickListener clickListener) {
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_help_me_craw, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull HelpMeCraw helpMeCraw) {

        Context context = holder.itemView.getContext();

        if (!TextUtils.isEmpty(helpMeCraw.getIcon())) {
            Glide.with(context).load(helpMeCraw.getIcon()).apply(RequestOptions.circleCropTransform()).into(holder.mImgAvatar);
        }
        holder.mTxtName.setText(helpMeCraw.getNickName());
        holder.mTxtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null)
                    mClickListener.onHelpCLick(getPosition(holder));
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_avatar) ImageView mImgAvatar;
        @BindView(R.id.txt_name) TextView mTxtName;
        @BindView(R.id.txt_help) TextView mTxtHelp;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
