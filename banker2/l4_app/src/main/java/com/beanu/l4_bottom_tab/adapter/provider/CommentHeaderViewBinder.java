package com.beanu.l4_bottom_tab.adapter.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.CommentHeader;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 评论列表 header
 * Created by Beanu on 2017/3/30.
 */
public class CommentHeaderViewBinder extends ItemViewBinder<CommentHeader, CommentHeaderViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_comment_header, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CommentHeader commentHeader) {
        holder.mTxtHeaderTotal.setText(String.format("共有%s条评论", commentHeader.getCommentCount()));
        holder.mRatingBar.setRating((float) commentHeader.getCommentRate());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_header_total) TextView mTxtHeaderTotal;
        @BindView(R.id.ratingBar) RatingBar mRatingBar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
