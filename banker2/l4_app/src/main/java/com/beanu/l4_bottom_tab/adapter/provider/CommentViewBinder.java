package com.beanu.l4_bottom_tab.adapter.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.Comment;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 评论ITEM
 * Created by Beanu on 2017/3/30.
 */
public class CommentViewBinder extends ItemViewBinder<Comment, CommentViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Comment comment) {
        holder.mTxtName.setText(comment.getNickName());
        holder.mTxtTime.setText(comment.getCreatetime());
        holder.mTxtContent.setText(comment.getContent());
        holder.mRatingBar.setRating(comment.getStar_rating());

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name) TextView mTxtName;
        @BindView(R.id.ratingBar) RatingBar mRatingBar;
        @BindView(R.id.txt_date) TextView mTxtTime;
        @BindView(R.id.txt_content) TextView mTxtContent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
